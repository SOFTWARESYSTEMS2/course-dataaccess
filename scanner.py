import os
import re
import json
import xml.etree.ElementTree as ET
from pathlib import Path

# =========================
# Config
# =========================

SKIP_DIRS = {
    ".git", ".idea", ".vscode", "target", "build", "out", "node_modules",
    ".gradle", ".mvn"
}

JAVA_EXTENSIONS = {".java", ".kt"}
RESOURCE_FILES = {
    "application.properties", "application.yml", "application.yaml"
}

CLASS_ANNOTATIONS_OF_INTEREST = {
    "Controller", "RestController", "Service", "Repository", "Component",
    "Configuration", "Entity", "MappedSuperclass", "Embeddable",
    "SpringBootApplication"
}

METHOD_MAPPING_ANNOTATIONS = {
    "RequestMapping", "GetMapping", "PostMapping", "PutMapping",
    "DeleteMapping", "PatchMapping"
}

FIELD_ANNOTATIONS_OF_INTEREST = {
    "Autowired", "Value", "Id", "GeneratedValue", "OneToMany", "ManyToOne",
    "ManyToMany", "OneToOne", "JoinColumn", "Column", "Transient"
}

OUTPUT_FILE = "spring_project_structure.json"


# =========================
# Regex helpers
# =========================

PACKAGE_RE = re.compile(r'^\s*package\s+([\w\.]+)\s*;', re.MULTILINE)
IMPORT_RE = re.compile(r'^\s*import\s+([\w\.\*]+)\s*;', re.MULTILINE)

ANNOTATION_RE = re.compile(r'^\s*@(\w+)(\((.*?)\))?', re.MULTILINE)

CLASS_RE = re.compile(
    r'((?:@\w+(?:\([^)]*\))?\s*)*)'
    r'(?:public|protected|private|abstract|final|static|\s)*'
    r'\b(class|interface|enum|record)\s+(\w+)'
    r'(?:\s+extends\s+[\w\<\>\.,\s]+)?'
    r'(?:\s+implements\s+[\w\<\>\.,\s]+)?'
    r'\s*\{',
    re.MULTILINE
)

METHOD_RE = re.compile(
    r'((?:@\w+(?:\([^)]*\))?\s*)*)'
    r'^\s*(public|protected|private)?\s*'
    r'(?:static\s+)?(?:final\s+)?'
    r'([\w\<\>\[\],\.\?\s]+?)\s+'
    r'(\w+)\s*'
    r'\(([^)]*)\)\s*'
    r'(?:throws\s+[^{]+)?'
    r'\{',
    re.MULTILINE
)

FIELD_RE = re.compile(
    r'((?:@\w+(?:\([^)]*\))?\s*)*)'
    r'^\s*(private|protected|public)?\s*'
    r'(?:static\s+)?(?:final\s+)?'
    r'([\w\<\>\[\],\.\?]+)\s+'
    r'(\w+)\s*'
    r'(?:=\s*[^;]+)?;',
    re.MULTILINE
)

REQUEST_MAPPING_VALUE_RE = re.compile(
    r'@(?:RequestMapping|GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping)'
    r'\((.*?)\)',
    re.DOTALL
)

PATH_VALUE_RE = re.compile(
    r'(?:value|path)\s*=\s*\{?\s*"([^"]+)"'
)

DIRECT_PATH_RE = re.compile(
    r'^\s*"([^"]+)"'
)


# =========================
# Utilities
# =========================

def safe_read_text(path: Path) -> str:
    try:
        return path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        try:
            return path.read_text(encoding="latin-1")
        except Exception:
            return ""
    except Exception:
        return ""


def parse_annotation_block(annotation_block: str):
    annotations = []
    for match in ANNOTATION_RE.finditer(annotation_block or ""):
        name = match.group(1)
        args = (match.group(3) or "").strip()
        annotations.append({"name": name, "args": args})
    return annotations


def extract_mapping_paths(annotation_text: str):
    paths = []

    for match in REQUEST_MAPPING_VALUE_RE.finditer(annotation_text):
        content = match.group(1).strip()

        explicit = PATH_VALUE_RE.findall(content)
        if explicit:
            paths.extend(explicit)
            continue

        direct = DIRECT_PATH_RE.match(content)
        if direct:
            paths.append(direct.group(1))

    return paths


def endpoint_http_method(annotation_names):
    for ann in annotation_names:
        if ann == "GetMapping":
            return ["GET"]
        if ann == "PostMapping":
            return ["POST"]
        if ann == "PutMapping":
            return ["PUT"]
        if ann == "DeleteMapping":
            return ["DELETE"]
        if ann == "PatchMapping":
            return ["PATCH"]
        if ann == "RequestMapping":
            return ["ANY"]
    return []


def parse_properties(text: str):
    props = {}
    for line in text.splitlines():
        line = line.strip()
        if not line or line.startswith("#") or line.startswith("!"):
            continue
        if "=" in line:
            key, value = line.split("=", 1)
            props[key.strip()] = value.strip()
        elif ":" in line:
            key, value = line.split(":", 1)
            props[key.strip()] = value.strip()
    return props


def parse_pom_xml(path: Path):
    result = {
        "groupId": None,
        "artifactId": None,
        "version": None,
        "dependencies": []
    }

    try:
        tree = ET.parse(path)
        root = tree.getroot()

        ns = ""
        if root.tag.startswith("{"):
            ns = root.tag.split("}")[0] + "}"

        def find_text(tag):
            elem = root.find(f"{ns}{tag}")
            return elem.text.strip() if elem is not None and elem.text else None

        result["groupId"] = find_text("groupId")
        result["artifactId"] = find_text("artifactId")
        result["version"] = find_text("version")

        if result["groupId"] is None:
            parent = root.find(f"{ns}parent")
            if parent is not None:
                group = parent.find(f"{ns}groupId")
                if group is not None and group.text:
                    result["groupId"] = group.text.strip()

        if result["version"] is None:
            parent = root.find(f"{ns}parent")
            if parent is not None:
                version = parent.find(f"{ns}version")
                if version is not None and version.text:
                    result["version"] = version.text.strip()

        deps = root.find(f"{ns}dependencies")
        if deps is not None:
            for dep in deps.findall(f"{ns}dependency"):
                group = dep.find(f"{ns}groupId")
                artifact = dep.find(f"{ns}artifactId")
                version = dep.find(f"{ns}version")
                scope = dep.find(f"{ns}scope")

                result["dependencies"].append({
                    "groupId": group.text.strip() if group is not None and group.text else None,
                    "artifactId": artifact.text.strip() if artifact is not None and artifact.text else None,
                    "version": version.text.strip() if version is not None and version.text else None,
                    "scope": scope.text.strip() if scope is not None and scope.text else None,
                })

    except Exception as e:
        result["error"] = str(e)

    return result


def parse_gradle(text: str):
    dependencies = []
    dependency_re = re.compile(
        r'^\s*(implementation|api|compileOnly|runtimeOnly|testImplementation|testRuntimeOnly|annotationProcessor)\s+["\']([^"\']+)["\']',
        re.MULTILINE
    )

    plugins = []
    plugin_re = re.compile(r'id\s+["\']([^"\']+)["\']')

    for match in dependency_re.finditer(text):
        dependencies.append({
            "configuration": match.group(1),
            "notation": match.group(2)
        })

    for match in plugin_re.finditer(text):
        plugins.append(match.group(1))

    return {
        "plugins": plugins,
        "dependencies": dependencies
    }


# =========================
# Source parsing
# =========================

def parse_source_file(path: Path):
    text = safe_read_text(path)
    if not text:
        return None

    package_match = PACKAGE_RE.search(text)
    package_name = package_match.group(1) if package_match else None

    imports = IMPORT_RE.findall(text)

    classes = []
    for class_match in CLASS_RE.finditer(text):
        annotation_block = class_match.group(1) or ""
        class_type = class_match.group(2)
        class_name = class_match.group(3)

        class_annotations = parse_annotation_block(annotation_block)
        class_annotation_names = [a["name"] for a in class_annotations]

        classes.append({
            "name": class_name,
            "type": class_type,
            "annotations": class_annotations,
            "roles": [a for a in class_annotation_names if a in CLASS_ANNOTATIONS_OF_INTEREST]
        })

    methods = []
    for method_match in METHOD_RE.finditer(text):
        annotation_block = method_match.group(1) or ""
        visibility = method_match.group(2)
        return_type = " ".join(method_match.group(3).split())
        method_name = method_match.group(4)
        params = " ".join(method_match.group(5).split())

        annotations = parse_annotation_block(annotation_block)
        annotation_names = [a["name"] for a in annotations]

        method_info = {
            "name": method_name,
            "visibility": visibility,
            "return_type": return_type,
            "params": params,
            "annotations": annotations
        }

        if any(a in METHOD_MAPPING_ANNOTATIONS for a in annotation_names):
            method_info["endpoint"] = {
                "http_methods": endpoint_http_method(annotation_names),
                "paths": extract_mapping_paths(annotation_block)
            }

        methods.append(method_info)

    fields = []
    for field_match in FIELD_RE.finditer(text):
        annotation_block = field_match.group(1) or ""
        visibility = field_match.group(2)
        field_type = field_match.group(3)
        field_name = field_match.group(4)

        annotations = parse_annotation_block(annotation_block)
        annotation_names = [a["name"] for a in annotations]

        fields.append({
            "name": field_name,
            "type": field_type,
            "visibility": visibility,
            "annotations": annotations,
            "interesting_annotations": [a for a in annotation_names if a in FIELD_ANNOTATIONS_OF_INTEREST]
        })

    top_level_request_paths = []
    for cls in classes:
        ann_text = ""
        for ann in cls["annotations"]:
            if ann["name"] == "RequestMapping":
                args = ann["args"]
                ann_text += f'@RequestMapping({args})\n'
        top_level_request_paths.extend(extract_mapping_paths(ann_text))

    return {
        "file": str(path),
        "package": package_name,
        "imports": imports,
        "classes": classes,
        "fields": fields,
        "methods": methods,
        "class_level_paths": top_level_request_paths
    }


# =========================
# Project scan
# =========================

def should_skip_dir(dirname: str) -> bool:
    return dirname in SKIP_DIRS


def scan_project(root_dir: str):
    root = Path(root_dir).resolve()

    result = {
        "project_root": str(root),
        "build_files": {},
        "resources": {
            "properties": {},
            "yaml_files": [],
            "templates": [],
            "static_files": []
        },
        "source_files": [],
        "summary": {
            "packages": set(),
            "controllers": [],
            "services": [],
            "repositories": [],
            "entities": [],
            "configurations": [],
            "components": [],
            "endpoints": []
        }
    }

    for dirpath, dirnames, filenames in os.walk(root):
        dirnames[:] = [d for d in dirnames if not should_skip_dir(d)]
        current = Path(dirpath)

        for filename in filenames:
            path = current / filename

            # Build files
            if filename == "pom.xml":
                result["build_files"]["pom.xml"] = parse_pom_xml(path)
                continue
            if filename in {"build.gradle", "build.gradle.kts"}:
                result["build_files"][filename] = parse_gradle(safe_read_text(path))
                continue

            # Resource files
            if filename in RESOURCE_FILES:
                text = safe_read_text(path)
                if filename.endswith(".properties"):
                    result["resources"]["properties"][str(path)] = parse_properties(text)
                else:
                    result["resources"]["yaml_files"].append(str(path))
                continue

            # Templates / static
            lowered_parts = {p.lower() for p in path.parts}
            if "templates" in lowered_parts:
                result["resources"]["templates"].append(str(path))
            if "static" in lowered_parts:
                result["resources"]["static_files"].append(str(path))

            # Source files
            if path.suffix in JAVA_EXTENSIONS:
                parsed = parse_source_file(path)
                if not parsed:
                    continue

                result["source_files"].append(parsed)

                if parsed["package"]:
                    result["summary"]["packages"].add(parsed["package"])

                class_level_paths = parsed.get("class_level_paths", [])

                for cls in parsed["classes"]:
                    roles = set(cls["roles"])
                    entry = {
                        "file": parsed["file"],
                        "package": parsed["package"],
                        "name": cls["name"],
                        "type": cls["type"]
                    }

                    if "Controller" in roles or "RestController" in roles:
                        result["summary"]["controllers"].append(entry)
                    if "Service" in roles:
                        result["summary"]["services"].append(entry)
                    if "Repository" in roles:
                        result["summary"]["repositories"].append(entry)
                    if "Entity" in roles:
                        result["summary"]["entities"].append(entry)
                    if "Configuration" in roles or "SpringBootApplication" in roles:
                        result["summary"]["configurations"].append(entry)
                    if "Component" in roles:
                        result["summary"]["components"].append(entry)

                for method in parsed["methods"]:
                    if "endpoint" in method:
                        result["summary"]["endpoints"].append({
                            "file": parsed["file"],
                            "package": parsed["package"],
                            "method_name": method["name"],
                            "return_type": method["return_type"],
                            "class_level_paths": class_level_paths,
                            "method_paths": method["endpoint"]["paths"],
                            "http_methods": method["endpoint"]["http_methods"]
                        })

    result["summary"]["packages"] = sorted(result["summary"]["packages"])
    return result


# =========================
# Main
# =========================

if __name__ == "__main__":
    import argparse

    parser = argparse.ArgumentParser(description="Scan a Spring Boot project structure.")
    parser.add_argument(
        "project_root",
        nargs="?",
        default=".",
        help="Path to the Spring Boot project root"
    )
    parser.add_argument(
        "--out",
        default=OUTPUT_FILE,
        help="Output JSON file"
    )

    args = parser.parse_args()

    data = scan_project(args.project_root)

    with open(args.out, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2)

    print(f"Done. Wrote project structure to: {args.out}")
    print()
    print("Summary:")
    print(f"  Packages:       {len(data['summary']['packages'])}")
    print(f"  Controllers:    {len(data['summary']['controllers'])}")
    print(f"  Services:       {len(data['summary']['services'])}")
    print(f"  Repositories:   {len(data['summary']['repositories'])}")
    print(f"  Entities:       {len(data['summary']['entities'])}")
    print(f"  Configurations: {len(data['summary']['configurations'])}")
    print(f"  Components:     {len(data['summary']['components'])}")
    print(f"  Endpoints:      {len(data['summary']['endpoints'])}")