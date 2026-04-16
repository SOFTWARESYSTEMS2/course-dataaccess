package Degree_Flowchart.course_dataaccess.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import Degree_Flowchart.course_dataaccess.data.entities.Course;
import Degree_Flowchart.course_dataaccess.data.entities.CourseOffering;
import Degree_Flowchart.course_dataaccess.data.entities.Department;
import Degree_Flowchart.course_dataaccess.data.entities.Prerequisite;
import Degree_Flowchart.course_dataaccess.data.entities.Term;
import Degree_Flowchart.course_dataaccess.data.repositories.CourseOfferingRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.CourseRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.DepartmentRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.PrerequisiteRepository;
import Degree_Flowchart.course_dataaccess.data.repositories.TermRepository;

@Configuration
public class CourseAccessDataLoader {

    @Bean
    CommandLineRunner loadCourseAccessData(
            DepartmentRepository deptRepo,
            TermRepository termRepo,
            CourseRepository courseRepo,
            PrerequisiteRepository prereqRepo,
            CourseOfferingRepository offeringRepo
    ) {
        return args -> {
            if (deptRepo.count() > 0) return;

            // Departments
            Department cs    = deptRepo.save(new Department("Computer Science"));
            Department math  = deptRepo.save(new Department("Mathematics"));
            Department eng   = deptRepo.save(new Department("English"));
            Department bio   = deptRepo.save(new Department("Biology"));
            Department chem  = deptRepo.save(new Department("Chemistry"));
            Department hist  = deptRepo.save(new Department("History"));
            Department phys  = deptRepo.save(new Department("Physics"));
            Department psych = deptRepo.save(new Department("Psychology"));
            Department stat  = deptRepo.save(new Department("Statistics"));
            Department info  = deptRepo.save(new Department("Information Science"));

            // Terms
            Term y1s1 = termRepo.save(new Term("Year 1, Semester 1", false));
            Term y1s2 = termRepo.save(new Term("Year 1, Semester 2", false));
            Term y2s1 = termRepo.save(new Term("Year 2, Semester 1", true));
            Term y2s2 = termRepo.save(new Term("Year 2, Semester 2", false));
            Term y3s1 = termRepo.save(new Term("Year 3, Semester 1", false));
            Term y3s2 = termRepo.save(new Term("Year 3, Semester 2", false));
            Term y4s1 = termRepo.save(new Term("Year 4, Semester 1", false));
            Term y4s2 = termRepo.save(new Term("Year 4, Semester 2", false));
            List<Term> allTerms = List.of(y1s1, y1s2, y2s1, y2s2, y3s1, y3s2, y4s1, y4s2);

            // Computer Science (12)
            Course csA101 = courseRepo.save(new Course("CSCI-A101", "Intro to Programming", 3, cs, false));
            Course csA201 = courseRepo.save(new Course("CSCI-A201", "Data Structures", 3, cs, false));
            Course csA301 = courseRepo.save(new Course("CSCI-A301", "Algorithms", 3, cs, false));
            Course csA310 = courseRepo.save(new Course("CSCI-A310", "Software Engineering", 3, cs, false));
            Course csA340 = courseRepo.save(new Course("CSCI-A340", "Artificial Intelligence", 3, cs, false));
            Course csA401 = courseRepo.save(new Course("CSCI-A401", "Operating Systems", 3, cs, false));
            Course csA422 = courseRepo.save(new Course("CSCI-A422", "Computer Networks", 3, cs, false));
            Course csA442 = courseRepo.save(new Course("CSCI-A442", "Database Systems", 3, cs, false));
            Course csA490 = courseRepo.save(new Course("CSCI-A490", "Senior Capstone", 3, cs, false));
            Course csB461 = courseRepo.save(new Course("CSCI-B461", "Machine Learning", 3, cs, false));
            Course csC241 = courseRepo.save(new Course("CSCI-C241", "Discrete Mathematics", 3, cs, false));
            Course csA348 = courseRepo.save(new Course("CSCI-A348", "Web Development", 3, cs, false));

            // Mathematics (9)
            Course mM118  = courseRepo.save(new Course("MATH-M118", "Finite Mathematics",            3, math, false));
            Course mM211  = courseRepo.save(new Course("MATH-M211", "Calculus I",                    4, math, false));
            Course mM212  = courseRepo.save(new Course("MATH-M212", "Calculus II",                   4, math, false));
            Course mM301  = courseRepo.save(new Course("MATH-M301", "Linear Algebra",                3, math, false));
            Course mM311  = courseRepo.save(new Course("MATH-M311", "Calculus III",                  4, math, false));
            Course mM360  = courseRepo.save(new Course("MATH-M360", "Elements of Probability",       3, math, false));
            Course mM371  = courseRepo.save(new Course("MATH-M371", "Complex Variables",             3, math, false));
            Course mM403  = courseRepo.save(new Course("MATH-M403", "Abstract Algebra",              3, math, false));
            Course mM447  = courseRepo.save(new Course("MATH-M447", "Mathematical Analysis",         3, math, false));

            // Statistics (4)
            Course stS301 = courseRepo.save(new Course("STAT-S301", "Applied Statistical Methods",   3, stat, false));
            Course stS420 = courseRepo.save(new Course("STAT-S420", "Statistics Theory",             3, stat, false));
            Course stS431 = courseRepo.save(new Course("STAT-S431", "Applied Linear Models",         3, stat, false));
            Course stS440 = courseRepo.save(new Course("STAT-S440", "Time Series Analysis",          3, stat, false));

            // English (7)
            Course eL101  = courseRepo.save(new Course("ENG-L101",  "Reading and Writing",           3, eng, false));
            Course eL201  = courseRepo.save(new Course("ENG-L201",  "Introduction to Literature",    3, eng, false));
            Course eL301  = courseRepo.save(new Course("ENG-L301",  "British Literature",            3, eng, false));
            Course eL302  = courseRepo.save(new Course("ENG-L302",  "American Literature",           3, eng, false));
            Course eL401  = courseRepo.save(new Course("ENG-L401",  "Senior Seminar",                3, eng, false));
            Course eW231  = courseRepo.save(new Course("ENG-W231",  "Technical Writing",             3, eng, false));
            Course eW350  = courseRepo.save(new Course("ENG-W350",  "Creative Writing",              3, eng, false));

            // Biology (6)
            Course bL101  = courseRepo.save(new Course("BIOL-L101", "Principles of Biology I",       3, bio, false));
            Course bL102  = courseRepo.save(new Course("BIOL-L102", "Principles of Biology II",      3, bio, false));
            Course bL211  = courseRepo.save(new Course("BIOL-L211", "Cell Biology",                  3, bio, false));
            Course bL311  = courseRepo.save(new Course("BIOL-L311", "Genetics",                      3, bio, false));
            Course bL401  = courseRepo.save(new Course("BIOL-L401", "Ecology",                       3, bio, false));
            Course bL450  = courseRepo.save(new Course("BIOL-L450", "Bioinformatics",                3, bio, false));

            // Chemistry (5)
            Course cC101  = courseRepo.save(new Course("CHEM-C101", "General Chemistry I",           3, chem, false));
            Course cC102  = courseRepo.save(new Course("CHEM-C102", "General Chemistry II",          3, chem, false));
            Course cC301  = courseRepo.save(new Course("CHEM-C301", "Organic Chemistry I",           3, chem, false));
            Course cC302  = courseRepo.save(new Course("CHEM-C302", "Organic Chemistry II",          3, chem, false));
            Course cC401  = courseRepo.save(new Course("CHEM-C401", "Biochemistry",                  3, chem, false));

            // History (5)
            Course hH101  = courseRepo.save(new Course("HIST-H101", "World History I",               3, hist, false));
            Course hH102  = courseRepo.save(new Course("HIST-H102", "World History II",              3, hist, false));
            Course hH301  = courseRepo.save(new Course("HIST-H301", "American History",              3, hist, false));
            Course hH350  = courseRepo.save(new Course("HIST-H350", "European History",              3, hist, false));
            Course hH401  = courseRepo.save(new Course("HIST-H401", "Senior Research Seminar",       3, hist, false));

            // Physics (5)
            Course pP201  = courseRepo.save(new Course("PHYS-P201", "Physics I",                     4, phys, false));
            Course pP202  = courseRepo.save(new Course("PHYS-P202", "Physics II",                    4, phys, false));
            Course pP301  = courseRepo.save(new Course("PHYS-P301", "Modern Physics",                3, phys, false));
            Course pP401  = courseRepo.save(new Course("PHYS-P401", "Quantum Mechanics",             3, phys, false));
            Course pP310  = courseRepo.save(new Course("PHYS-P310", "Thermodynamics",                3, phys, false));

            // Psychology (5)
            Course pyP101 = courseRepo.save(new Course("PSY-P101",  "Intro to Psychology",           3, psych, false));
            Course pyP211 = courseRepo.save(new Course("PSY-P211",  "Research Methods",              3, psych, false));
            Course pyP301 = courseRepo.save(new Course("PSY-P301",  "Abnormal Psychology",           3, psych, false));
            Course pyP320 = courseRepo.save(new Course("PSY-P320",  "Social Psychology",             3, psych, false));
            Course pyP401 = courseRepo.save(new Course("PSY-P401",  "Capstone in Psychology",        3, psych, false));

            // Information Science (5)
            Course iI101  = courseRepo.save(new Course("INFO-I101",  "Intro to Informatics",         3, info, false));
            Course iI201  = courseRepo.save(new Course("INFO-I201",  "Data Representation",          3, info, false));
            Course iI301  = courseRepo.save(new Course("INFO-I301",  "Human-Computer Interaction",   3, info, false));
            Course iI308  = courseRepo.save(new Course("INFO-I308",  "Web Design",                   3, info, false));
            Course iI401  = courseRepo.save(new Course("INFO-I401",  "Information Architecture",     3, info, false));


            // ── Prerequisites ──────────────────────────────────────────────
            // CS chain
            prereqRepo.save(new Prerequisite(csA201, csA101));
            prereqRepo.save(new Prerequisite(csA301, csA201));
            prereqRepo.save(new Prerequisite(csA310, csA201));
            prereqRepo.save(new Prerequisite(csA340, csA201));
            prereqRepo.save(new Prerequisite(csA401, csA301));
            prereqRepo.save(new Prerequisite(csA422, csA301));
            prereqRepo.save(new Prerequisite(csA442, csA201));
            prereqRepo.save(new Prerequisite(csA490, csA401));
            prereqRepo.save(new Prerequisite(csB461, csA340));
            prereqRepo.save(new Prerequisite(csC241, csA101));
            prereqRepo.save(new Prerequisite(csA348, csA201));
            // Math chain
            prereqRepo.save(new Prerequisite(mM212, mM211));
            prereqRepo.save(new Prerequisite(mM301, mM211));
            prereqRepo.save(new Prerequisite(mM311, mM212));
            prereqRepo.save(new Prerequisite(mM360, mM212));
            prereqRepo.save(new Prerequisite(mM371, mM311));
            prereqRepo.save(new Prerequisite(mM403, mM301));
            prereqRepo.save(new Prerequisite(mM447, mM311));
            // Stat
            prereqRepo.save(new Prerequisite(stS301, mM118));
            prereqRepo.save(new Prerequisite(stS420, mM211));
            prereqRepo.save(new Prerequisite(stS431, stS301));
            prereqRepo.save(new Prerequisite(stS440, stS420));
            // English
            prereqRepo.save(new Prerequisite(eL201, eL101));
            prereqRepo.save(new Prerequisite(eL301, eL201));
            prereqRepo.save(new Prerequisite(eL302, eL201));
            prereqRepo.save(new Prerequisite(eL401, eL301));
            prereqRepo.save(new Prerequisite(eW350, eL201));
            // Biology
            prereqRepo.save(new Prerequisite(bL102, bL101));
            prereqRepo.save(new Prerequisite(bL211, bL102));
            prereqRepo.save(new Prerequisite(bL311, bL211));
            prereqRepo.save(new Prerequisite(bL401, bL311));
            prereqRepo.save(new Prerequisite(bL450, bL311));
            // Chemistry
            prereqRepo.save(new Prerequisite(cC102, cC101));
            prereqRepo.save(new Prerequisite(cC301, cC102));
            prereqRepo.save(new Prerequisite(cC302, cC301));
            prereqRepo.save(new Prerequisite(cC401, cC301));
            // History
            prereqRepo.save(new Prerequisite(hH102, hH101));
            prereqRepo.save(new Prerequisite(hH401, hH301));
            // Physics (needs Calc I)
            prereqRepo.save(new Prerequisite(pP201, mM211));
            prereqRepo.save(new Prerequisite(pP202, pP201));
            prereqRepo.save(new Prerequisite(pP301, pP202));
            prereqRepo.save(new Prerequisite(pP401, pP301));
            prereqRepo.save(new Prerequisite(pP310, pP202));
            // Psychology
            prereqRepo.save(new Prerequisite(pyP211, pyP101));
            prereqRepo.save(new Prerequisite(pyP301, pyP211));
            prereqRepo.save(new Prerequisite(pyP320, pyP211));
            prereqRepo.save(new Prerequisite(pyP401, pyP301));
            // Info Science
            prereqRepo.save(new Prerequisite(iI201, iI101));
            prereqRepo.save(new Prerequisite(iI301, iI201));
            prereqRepo.save(new Prerequisite(iI308, iI201));
            prereqRepo.save(new Prerequisite(iI401, iI301));

            List<Course> allCourses = courseRepo.findAll();

            // Offerings
            for (Term term : allTerms) {
                for (Course course : allCourses) {
                    int capacity = 30;
                    int enrolled = 0;

                    if (course.getCode().equals("CSCI-A101")
                            || course.getCode().equals("PSY-P101")
                            || course.getCode().equals("ENG-L101")
                            || course.getCode().equals("BIOL-L101")) {
                        capacity = 120;
                    }

                    if (course.getCode().endsWith("401")
                            || course.getCode().endsWith("490")
                            || course.getCode().endsWith("447")
                            || course.getCode().endsWith("461")) {
                        capacity = 15;
                    }

                    if (course.getCode().equals("CSCI-A201") && term.getLabel().equals("Year 2, Semester 1")) {
                        capacity = 25;
                        enrolled = 25;
                    }

                    if (course.getCode().equals("MATH-M212") && term.getLabel().equals("Year 2, Semester 1")) {
                        capacity = 20;
                        enrolled = 20;
                    }

                    offeringRepo.save(new CourseOffering(course, term, capacity, enrolled));
                }
            }
        };
    }
}