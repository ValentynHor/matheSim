package val.hor.simulator.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import val.hor.simulator.entity.skill.Skill;
import val.hor.simulator.repository.*;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StartAllTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private SkillRepository skillRepo;
    @Autowired
    public TaskRepository taskRepo;
    @Autowired
    AnswerRepository answerRepo;
    @Autowired
    private SettingsRepository settingsRepo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Transactional
    @Test
    public void dropAllTables() {
        entityManager.createNativeQuery(
                "DROP TABLE IF EXISTS answers, teacher_student, exam_tasks, progress," +
                        " skills, tasks, tasks_answers, user_roles," +
                        " users,roles, exams, results," +
                        "  CASCADE").executeUpdate();
    }


    public void createFirstPeople(){
        createRole();
        Role roleAdmin = testEntityManager.find(Role.class, 1);
        User user = new User(
                "valik@gmail.com",
                "$2a$10$R3zMwKSw.11/R9snN/EEWOGNB4aXjec3xsm52KCw7PA6HV3.xJsZ2",
                "Admin",
                "AAA");
        user.setEnabled(true);
        user.addRole(roleAdmin);
        userRepo.save(user);

        Role roleTeacher = testEntityManager.find(Role.class, 2);
        User user2 = new User(
                "teacher@gmail.com",
                "$2a$10$R3zMwKSw.11/R9snN/EEWOGNB4aXjec3xsm52KCw7PA6HV3.xJsZ2",
                "Teacher",
                "TTT");
        user2.setEnabled(true);
        user2.addRole(roleTeacher);
        userRepo.save(user2);

        Role roleStudent = testEntityManager.find(Role.class, 3);
        User user3= new User(
                "valentyn.hordiychuk@gmail.com",
                "$2a$10$R3zMwKSw.11/R9snN/EEWOGNB4aXjec3xsm52KCw7PA6HV3.xJsZ2",
                "Teacher",
                "TTT");
        user3.setEnabled(true);
        user3.addRole(roleStudent);
        userRepo.save(user3);

        create50User();
    }

    @Test
    public void createAll(){
        //User + Role
        createFirstPeople();
        //Skills
        createFirstSkill();
        createSubSkill();
        createSubSub_AG();
        createSubSub_FA();
        createSubSub_AN();
        createSubSub_WS();
        //Task+Answer
        createTask1();
        createTask2();
        createTask3();
        createTask4();
        createTask5();
        createAnswerTask1();
        createAnswerTask2();
        createAnswerTask3();
        createAnswerTask4();
        createAnswerTask5();
        //
    }
    public void createRole(){
        Role roleAdmin = new Role("Admin","manage all");
        roleRepo.save(roleAdmin);

        Role roleTeacher = new Role("Teacher","manage all students");
        roleRepo.save(roleTeacher);

        Role roleStudent = new Role("Student","studies math");
        roleRepo.save(roleStudent);
    }
    public void create50User(){

        Role roleAdmin = testEntityManager.find(Role.class, 1);
        Role roleTeacher = testEntityManager.find(Role.class, 2);
        Role roleStudent = testEntityManager.find(Role.class, 3);
        String email = "";
        String password = "";
        String firstName="";
        String lastName="";

        for (int i=0; i<49; i++){
            email = "user" + (i+1) + "@gmx.at";
            password = "$2a$10$R3zMwKSw.11/R9snN/EEWOGNB4aXjec3xsm52KCw7PA6HV3.xJsZ2";
            firstName = "firstName" + (i+1);
            lastName = "lastName" + (i+1);
            User user = new User(email, password, firstName, lastName);
            if(i<5){
                user.addRole(roleAdmin);
            }else if(i<15){
                user.addRole(roleTeacher);
            } else if (i<51) {
                user.addRole(roleStudent);
            }
            user.setEnabled(true);
            userRepo.save(user);
        }



    }
    public void createFirstSkill(){
        List<Skill> skillList = new ArrayList<>();
        skillList.add(new Skill("AG","Algebra und Geometrie"));
        skillList.add(new Skill("FA","Funktionale Abhängigkeiten"));
        skillList.add(new Skill("AN","Analysis"));
        skillList.add(new Skill("WS","Wahrscheinlichkeit und Statistik"));
        for(Skill skill : skillList){
            skill.setProgress(0);
        }
        skillRepo.saveAll(skillList);
    }
    public void createSubSkill(){

        Skill s1 = testEntityManager.find(Skill.class, 1);
        Skill s2 = testEntityManager.find(Skill.class, 2);
        Skill s3 = testEntityManager.find(Skill.class, 3);
        Skill s4 = testEntityManager.find(Skill.class, 4);

        List<Skill> skillList = new ArrayList<>();

        skillList.add(new Skill("AG 1","Grundbegriffe der Algebra",s1));
        skillList.add(new Skill("AG 2","(Un-)Gleichungen und Gleichungssysteme",s1));
        skillList.add(new Skill("AG 3","Vektoren",s1));
        skillList.add(new Skill("AG 4","Trigonometrie",s1));

        skillList.add(new Skill("FA 1","Funktionsbegriff, reelle Funktionen, Darstellungsformen und Eigenschaften",s2));
        skillList.add(new Skill("FA 2","Lineare Funktion \\([f(x)=k \\cdot x + d]\\)",s2));
        skillList.add(new Skill("FA 3","Potenzfunktion mit \\(f(x)=a \\cdot x^z\\) und Funktionen vom Typ \\(f(x)=a \\cdot x^z + b\\) mit \\(z \\in \\mathbb Z\\setminus\\{0\\}\\) oder \\(z=\\frac{1}{2}\\)",s2));
        skillList.add(new Skill("FA 4","Polynomfunktion[ @@@ mit @@@]",s2));
        skillList.add(new Skill("FA 5","Exponentialfunktion[@@@ bzw. @@@ mit @@@, @@@]",s2));
        skillList.add(new Skill("FA 6","Sinusfunktion, Cosinusfunktion",s2));

        skillList.add(new Skill("AN 1","Änderungsmaße",s3));
        skillList.add(new Skill("AN 2","Regeln für das Differenzieren",s3));
        skillList.add(new Skill("AN 3","Ableitungsfunktion/Stammfunktion",s3));
        skillList.add(new Skill("AN 4","Summation und Integral",s3));

        skillList.add(new Skill("WS 1","Beschreibende Statistik",s4));
        skillList.add(new Skill("WS 2","Wahrscheinlichkeitsrechnung",s4));
        skillList.add(new Skill("WS 3","Wahrscheinlichkeitsverteilung",s4));
        skillList.add(new Skill("WS 4","Schließende/beurteilende Statistik",s4));

        for(Skill skill : skillList){
            skill.setProgress(0);
        }

        skillRepo.saveAll(skillList);
    }
    public void createSubSub_AG(){

        Skill parent_AG1 = testEntityManager.find(Skill.class, 5);
        Skill parent_AG2 = testEntityManager.find(Skill.class, 6);
        Skill parent_AG3 = testEntityManager.find(Skill.class, 7);
        Skill parent_AG4 = testEntityManager.find(Skill.class, 8);

        List<Skill> skillList = new ArrayList<>();

        skillList.add(new Skill("AG 1.1","Wissen über die Zahlenmengen, -bereiche \\(\\mathbb N, \\mathbb Z, \\mathbb Q, \\mathbb R, \\mathbb C\\) verständig einsetzen können", parent_AG1));
        skillList.add(new Skill("AG 1.2","Wissen über algebraische Begriffe angemessen einsetzen können: Variablen, Terme, Formeln, (Un-)Gleichungen, Gleichungssysteme, Äquivalenz, Umformungen, Lösbarkeit", parent_AG1));

        skillList.add(new Skill("AG 2.1","einfache Terme und Formeln aufstellen, umformen und im Kontext deuten können", parent_AG2));
        skillList.add(new Skill("AG 2.2","lineare Gleichungen aufstellen, interpretieren, umformen/lösen und die Lösung im Kontext deuten können", parent_AG2));
        skillList.add(new Skill("AG 2.3","quadratische Gleichungen in einer Variablen umformen/lösen, über Lösungsfälle Bescheid wissen, Lösungen und Lösungsfälle (auch geometrisch) deuten können",parent_AG2));
        skillList.add(new Skill("AG 2.4","lineare Ungleichungen aufstellen, interpretieren, umformen/lösen, Lösungen (auch geometrisch) deuten können",parent_AG2));
        skillList.add(new Skill("AG 2.5","lineare Gleichungssysteme in zwei Variablen aufstellen, interpretieren, umformen/lösen, über Lösungsfälle Bescheid wissen, Lösungen und Lösungsfälle (auch geometrisch) deuten können",parent_AG2));

        skillList.add(new Skill("AG 3.1","Vektoren als Zahlentupel verständig einsetzen und im Kontext deuten können", parent_AG3));
        skillList.add(new Skill("AG 3.2","Vektoren geometrisch (als Punkte bzw. Pfeile) deuten und verständig einsetzen können", parent_AG3));
        skillList.add(new Skill("AG 3.3","Definitionen der Rechenoperationen mit Vektoren (Addition, Multiplikation mit einem Skalar, Skalarprodukt) kennen, Rechenoperationen verständig einsetzen und (auch geometrisch) deuten können",parent_AG3));
        skillList.add(new Skill("AG 3.4","Geraden in \\(\\mathbb R^2\\) durch Parameterdarstellungen und Gleichungen, in \\(\\mathbb R^3\\) durch Parameterdarstellungen angeben und diese Darstellungen interpretieren können; Lagebeziehungen (zwischen Geraden und zwischen Punkt und Gerade) analysieren, Schnittpunkte ermitteln können",parent_AG3));
        skillList.add(new Skill("AG 3.5","Normalvektoren in \\(\\mathbb R^2 \\)aufstellen, verständig einsetzen und interpretieren können",parent_AG3));

        skillList.add(new Skill("AG 4.1","Definitionen von Sinus, Cosinus und Tangens im rechtwinkeligen Dreieck kennen und zur Auflösung rechtwinkeliger Dreiecke einsetzen können", parent_AG4));
        skillList.add(new Skill("AG 4.2","Definitionen von Sinus und Cosinus für Winkel größer als \\(90^\\circ\\) kennen und einsetzen können",parent_AG4));

        for(Skill skill : skillList){
            skill.setProgress(1);
        }

        skillRepo.saveAll(skillList);
    }
    public void createSubSub_FA(){

        Skill parent_FA1 = testEntityManager.find(Skill.class, 9);
        Skill parent_FA2 = testEntityManager.find(Skill.class, 10);
        Skill parent_FA3 = testEntityManager.find(Skill.class, 11);
        Skill parent_FA4 = testEntityManager.find(Skill.class, 12);
        Skill parent_FA5 = testEntityManager.find(Skill.class, 13);
        Skill parent_FA6 = testEntityManager.find(Skill.class, 14);

        List<Skill> skillList = new ArrayList<>();

        skillList.add(new Skill("FA 1.1","für gegebene Zusammenhänge entscheiden können, ob man sie als Funktionen betrachten kann", parent_FA1));
        skillList.add(new Skill("FA 1.2","Formeln als Darstellung von Funktionen interpretieren und dem Funktionstyp\n" +
                "zuordnen können", parent_FA1));
        skillList.add(new Skill("FA 1.3","zwischen tabellarischen und grafischen Darstellungen funktionaler Zusammenhänge wechseln können", parent_FA1));
        skillList.add(new Skill("FA 1.4","aus Tabellen, Graphen1 und Gleichungen von Funktionen Werte(paare) ermitteln und im Kontext deuten können", parent_FA1));
        skillList.add(new Skill("FA 1.5","Eigenschaften von Funktionen erkennen, benennen, im Kontext deuten und zum Erstellen von Funktionsgraphen einsetzen können: Monotonie(wechsel), lokale Extrema, Wendepunkte, Periodizität, Achsensymmetrie, asymptotisches\n" +
                "Verhalten, Schnittpunkte mit den Achsen", parent_FA1));
        skillList.add(new Skill("FA 1.6","Schnittpunkte zweier Funktionsgraphen grafisch und rechnerisch ermitteln und im Kontext interpretieren können", parent_FA1));
        skillList.add(new Skill("FA 1.7","Funktionen als mathematische Modelle verstehen und damit verständig arbeiten können", parent_FA1));
        skillList.add(new Skill("FA 1.8","durch Gleichungen (Formeln) gegebene Funktionen mit mehreren Veränderlichen im Kontext deuten können, Funktionswerte ermitteln können", parent_FA1));
        skillList.add(new Skill("FA 1.9","einen Überblick über die wichtigsten (unten angeführten) Typen mathematischer Funktionen geben, ihre Eigenschaften vergleichen können", parent_FA1));

        skillList.add(new Skill("FA 2.1","verbal, tabellarisch, grafisch oder durch eine Gleichung (Formel) gegebene lineare Zusammenhänge als lineare Funktionen erkennen bzw. betrachten können; zwischen diesen Darstellungsformen wechseln können", parent_FA2));
        skillList.add(new Skill("FA 2.2","aus Tabellen, Graphen und Gleichungen linearer Funktionen Werte(paare) sowie die Parameter k und d ermitteln und im Kontext deuten können", parent_FA2));
        skillList.add(new Skill("FA 2.3","die Wirkung der Parameter k und d kennen und die Parameter in unterschiedlichen Kontexten deuten können", parent_FA2));
        skillList.add(new Skill("FA 2.4","wichtige Eigenschaften kennen und im Kontext deuten können: @@@; @@@", parent_FA2));
        skillList.add(new Skill("FA 2.5","die Angemessenheit einer Beschreibung mittels linearer Funktion bewerten", parent_FA2));
        skillList.add(new Skill("FA 2.6","direkte Proportionalität als lineare Funktion vom Typ @@@ beschreiben können", parent_FA2));

        skillList.add(new Skill("FA 3.1","verbal, tabellarisch, grafisch oder durch eine Gleichung (Formel) gegebene Zusammenhänge dieser Art als entsprechende Funktionen erkennen bzw. betrachten können; zwischen diesen Darstellungsformen wechseln können", parent_FA3));
        skillList.add(new Skill("FA 3.2","aus Tabellen, Graphen und Gleichungen dieser Funktionen Werte(paare) sowie die Parameter a und b ermitteln und im Kontext deuten können", parent_FA3));
        skillList.add(new Skill("FA 3.3","die Wirkung der Parameter a und b kennen und die Parameter im Kontext deuten können", parent_FA3));
        skillList.add(new Skill("FA 3.4","indirekte Proportionalität als Potenzfunktion vom Typ @@@ bzw. @@@ beschreiben können",  parent_FA3));

        skillList.add(new Skill("FA 4.1","typische Verläufe von Graphen in Abhängigkeit vom Grad der Polynomfunktion (er)kennen", parent_FA4));
        skillList.add(new Skill("FA 4.2","zwischen tabellarischen und grafischen Darstellungen von Zusammenhängen dieser Art wechseln können", parent_FA4));
        skillList.add(new Skill("FA 4.3","aus Tabellen, Graphen und Gleichungen von Polynomfunktionen Funktionswerte, aus Tabellen und Graphen sowie aus einer quadratischen Funktionsgleichung\n" +
                "Argumentwerte ermitteln können", parent_FA4));
        skillList.add(new Skill("FA 4.4","den Zusammenhang zwischen dem Grad der Polynomfunktion und der Anzahl der (möglichen) Null-, Extrem- und Wendestellen wissen", parent_FA4));

        skillList.add(new Skill("FA 5.1","verbal, tabellarisch, grafisch oder durch eine Gleichung (Formel) gegebene exponentielle Zusammenhänge als Exponentialfunktion erkennen bzw. betrachten können; zwischen diesen Darstellungsformen wechseln können", parent_FA5));
        skillList.add(new Skill("FA 5.2","aus Tabellen, Graphen und Gleichungen von Exponentialfunktionen Werte(paare) ermitteln und im Kontext deuten können", parent_FA5));
        skillList.add(new Skill("FA 5.3","die Wirkung der Parameter a und b bzw. @@@ kennen und die Parameter in unterschiedlichen Kontexten deuten können", parent_FA5));
        skillList.add(new Skill("FA 5.4","wichtige Eigenschaften @@@; @@@ kennen und im Kontext deuten können",  parent_FA5));
        skillList.add(new Skill("FA 5.5","die Begriffe Halbwertszeit und Verdoppelungszeit kennen, die entsprechenden\n" +
                "Werte berechnen und im Kontext deuten können", parent_FA5));
        skillList.add(new Skill("FA 5.6","die Angemessenheit einer Beschreibung mittels Exponentialfunktion bewerten können", parent_FA5));

        skillList.add(new Skill("FA 6.1","grafisch oder durch eine Gleichung (Formel) gegebene Zusammenhänge der Art @@@ als allgemeine Sinusfunktion erkennen bzw. betrachten\n" +
                "können; zwischen diesen Darstellungsformen wechseln können", parent_FA6));
        skillList.add(new Skill("FA 6.2","aus Graphen und Gleichungen von allgemeinen Sinusfunktionen Werte(paare) ermitteln und im Kontext deuten können", parent_FA6));
        skillList.add(new Skill("FA 6.3","die Wirkung der Parameter a und b kennen und die Parameter im Kontext\n" +
                "deuten können", parent_FA6));
        skillList.add(new Skill("FA 6.4","Periodizität als charakteristische Eigenschaft kennen und im Kontext deuten\n" +
                "können", parent_FA6));
        skillList.add(new Skill("FA 6.5","wissen, dass @@@", parent_FA6));
        skillList.add(new Skill("FA 6.6","wissen, dass gilt: @@@; @@@", parent_FA6));

        for(Skill skill : skillList){
            skill.setProgress(1);
        }

        skillRepo.saveAll(skillList);

    }
    public void createSubSub_AN() {

        Skill parent_AN1 = testEntityManager.find(Skill.class, 15);
        Skill parent_AN2 = testEntityManager.find(Skill.class, 16);
        Skill parent_AN3 = testEntityManager.find(Skill.class, 17);
        Skill parent_AN4 = testEntityManager.find(Skill.class, 18);


        List<Skill> skillList = new ArrayList<>();

        skillList.add(new Skill("AN 1.1", "absolute und relative (prozentuelle) Änderungsmaße unterscheiden und angemessen verwenden können", parent_AN1));
        skillList.add(new Skill("AN 1.2", "den Zusammenhang Differenzenquotient (mittlere Änderungsrate) – Differenzialquotient („momentane“ bzw. lokale Änderungsrate) auf der Grundlage eines intuitiven Grenzwertbegriffs kennen und diese Konzepte (verbal sowie in\n" +
                "formaler Schreibweise) auch kontextbezogen anwenden können", parent_AN1));
        skillList.add(new Skill("AN 1.3", "den Differenzen- und Differenzialquotienten in verschiedenen Kontexten deuten und entsprechende Sachverhalte durch den Differenzen- bzw. Differenzialquotienten beschreiben können", parent_AN1));
        skillList.add(new Skill("AN 1.4", "das systemdynamische Verhalten von Größen durch Differenzengleichungen beschreiben bzw. diese im Kontext deuten können", parent_AN1));

        skillList.add(new Skill("AN 2.1", "einfache Regeln des Differenzierens kennen und anwenden können: Potenzregel, Summenregel, Regeln für @@@ und @@@ (vgl. Inhaltsbereich Funktionale Abhängigkeiten)", parent_AN2));

        skillList.add(new Skill("AN 3.1", "die Begriffe Ableitungsfunktion und Stammfunktion kennen und zur Beschreibung von Funktionen einsetzen können", parent_AN3));
        skillList.add(new Skill("AN 3.2", "den Zusammenhang zwischen Funktion und Ableitungsfunktion (bzw. Funktion und Stammfunktion) in deren grafischer Darstellung (er)kennen und beschreiben können", parent_AN3));
        skillList.add(new Skill("AN 3.3", "Eigenschaften von Funktionen mithilfe der Ableitung(sfunktion) beschreiben können: Monotonie, lokale Extrema, Links- und Rechtskrümmung, Wendestellen", parent_AN3));

        skillList.add(new Skill("AN 4.1", "den Begriff des bestimmten Integrals als Grenzwert einer Summe von Produkten deuten und beschreiben können", parent_AN4));
        skillList.add(new Skill("AN 4.2", "einfache Regeln des Integrierens kennen und anwenden können: Potenzregel, Summenregel, @@@, @@@ (vgl. Inhaltsbereich Funktionale Abhängigkeiten), bestimmte Integrale von Polynomfunktionen ermitteln können", parent_AN4));
        skillList.add(new Skill("AN 4.3", "das bestimmte Integral in verschiedenen Kontexten deuten und entsprechende Sachverhalte durch Integrale beschreiben können", parent_AN4));

        for(Skill skill : skillList){
            skill.setProgress(1);
        }

        skillRepo.saveAll(skillList);

    }
    public void createSubSub_WS() {

        Skill parent_WS1 = testEntityManager.find(Skill.class, 19);
        Skill parent_WS2 = testEntityManager.find(Skill.class, 20);
        Skill parent_WS3 = testEntityManager.find(Skill.class, 21);
        Skill parent_WS4 = testEntityManager.find(Skill.class, 22);

        List<Skill> skillList = new ArrayList<>();

        skillList.add(new Skill("WS 1.1", "Werte aus tabellarischen und elementaren grafischen Darstellungen ablesen (bzw. zusammengesetzte Werte ermitteln, d. h. aus den Grafiken ablesbare Daten zur Berechnung weiterer Kennzahlen verwenden können) und im jeweiligen Kontext angemessen interpretieren können", parent_WS1));
        skillList.add(new Skill("WS 1.2", "Tabellen und einfache statistische Grafiken erstellen, zwischen Darstellungsformen wechseln können", parent_WS1));
        skillList.add(new Skill("WS 1.3", "statistische Kennzahlen (absolute und relative Häufigkeiten; arithmetisches Mittel, Median, Modus, Quartile, Spannweite, empirische Varianz/Standardabweichung) im jeweiligen Kontext interpretieren können; die angeführten Kennzahlen für einfache Datensätze ermitteln können", parent_WS1));
        skillList.add(new Skill("WS 1.4", "Definition und wichtige Eigenschaften des arithmetischen Mittels und des\n" +
                "Medians angeben und nutzen, Quartile ermitteln und interpretieren können, die Entscheidung für die Verwendung einer bestimmten Kennzahl begründen\n" +
                "können", parent_WS1));

        skillList.add(new Skill("WS 2.1", "Grundraum (Menge der möglichen Versuchsausgänge) und Ereignisse in angemessenen Situationen verbal bzw. formal angeben können", parent_WS2));
        skillList.add(new Skill("WS 2.2", "relative Häufigkeit als Schätzwert von Wahrscheinlichkeit verwenden und anwenden können", parent_WS2));
        skillList.add(new Skill("WS 2.3", "Wahrscheinlichkeiten unter der Verwendung der Laplace-Annahme (Laplace-Wahrscheinlichkeit) berechnen und interpretieren können, Additionsregel und Multiplikationsregel anwenden und interpretieren können", parent_WS2));
        skillList.add(new Skill("WS 2.4", "Binomialkoeffizienten berechnen und interpretieren können", parent_WS2));

        skillList.add(new Skill("WS 3.1", "die Begriffe Zufallsvariable, (Wahrscheinlichkeits-)Verteilung, Erwartungswert und Standardabweichung verständig deuten und einsetzen können", parent_WS3));
        skillList.add(new Skill("WS 3.2", "Binomialverteilung als Modell einer diskreten Verteilung kennen – Erwartungswert sowie Varianz/Standardabweichung binomialverteilter Zufallsgrößen ermitteln können, Wahrscheinlichkeitsverteilung binomialverteilter Zufallsgrößen angeben können, Arbeiten mit der Binomialverteilung in anwendungsorientierten Bereichen", parent_WS3));
        skillList.add(new Skill("WS 3.3", "Situationen erkennen und beschreiben können, in denen mit Binomialverteilung modelliert werden kann", parent_WS3));
        skillList.add(new Skill("WS 3.4", "Normalapproximation der Binomialverteilung interpretieren und anwenden\n" +
                "können", parent_WS3));

        skillList.add(new Skill("WS 4.1", "Konfidenzintervalle als Schätzung für eine Wahrscheinlichkeit oder einen unbekannten Anteil p interpretieren (frequentistische Deutung) und verwenden können, Berechnungen auf Basis der Binomialverteilung oder einer durch die Normalverteilung approximierten Binomialverteilung durchführen können", parent_WS4));

        for(Skill skill : skillList){
            skill.setProgress(1);
        }

        skillRepo.saveAll(skillList);
    }
    private void createTask1(){
        String name =  "Positive rationale Zahlen";
        String question = "Kreuzen Sie jene beiden Zahlen an, die Elemente dieser Zahlenmenge sind!";

        String task = "Gegeben ist die Zahlenmenge \\(\\mathbb Q^+ \\)";
        String variant = "2 aus 5";
        Task firstTask = new Task(name, question, task, variant);
        taskRepo.save(firstTask);
    }
    public void createTask2(){
        String name =  "Rechenoperationen";
        String question = "Kreuzen Sie die beiden Ausdrücke an, die auf jeden Fall eine natürliche Zahl als Ergebnis liefern";
        String task = "Gegeben sind zwei natürliche Zahlen \\(a\\) und \\(b\\), wobei gilt: \\(b \\neq 0.\\) ";
        String variant = "2 aus 5";
        Task task2 = new Task(name, question, task, variant);
        taskRepo.save(task2);
    }
    private void createTask3(){
        String name =  "Rationale Zahlen";
        String question = "Kreuzen Sie die beiden rationalen Zahlen an";

        String task = "Gegeben sind folgende Zahlen.";
        String variant = "2 aus 5";
        Task firstTask = new Task(name, question, task, variant);
        taskRepo.save(firstTask);
    }
    private void createTask4(){
        String name =  "Zahlen und Zahlenmengen";
        String question = "Kreuzen Sie die beiden zutreffenden Aussagen an";

        String task = "Gegeben sind Aussagen zu Zahlen.";
        String variant = "2 aus 5";
        Task firstTask = new Task(name, question, task, variant);
        taskRepo.save(firstTask);
    }
    private void createTask5(){
        String name =  "Zahlenmengen";
        String question = "Kreuzen Sie die beiden zutreffenden Aussagen an";

        String task = "Unterstehend werden Aussagen über Zahlen aus den Zahlenmengen \\(\\mathbb N, \\mathbb Z, \\mathbb Q, \\mathbb R\\) und \\(\\mathbb C\\) getroffen.";
        String variant = "2 aus 5";
        Task firstTask = new Task(name, question, task, variant);
        taskRepo.save(firstTask);
    }
    private void createAnswerTask1(){

        Task task1 = testEntityManager.find(Task.class, 1);
        List<Answer> answerList = new ArrayList<>();

        //   \(    \)
        answerList.add(new Answer("\\(\\sqrt{@@}\\)",false,"1;SQRT=R+", task1));
        answerList.add(new Answer("\\(@@\\)",false,"1;Q-", task1));
        answerList.add(new Answer("\\(@@\\)",false,"1;FPN-", task1));
        answerList.add(new Answer("\\(\\frac{\\pi}{@@}\\)",false,"1;N", task1));

        answerList.add(new Answer("\\(@@\\)",true,"1;FPN+", task1));
        answerList.add(new Answer("\\(\\sqrt{@@}\\)",true,"1;SQRT=N", task1));
        answerList.add(new Answer("\\(@@\\)",true,"1;Q+", task1));

        answerRepo.saveAll(answerList);
    }
    public void createAnswerTask2(){

        Task task2 = testEntityManager.find(Task.class, 2);
        List<Answer> answerList = new ArrayList<>();

        answerList.add(new Answer("\\(a - b\\)",false, task2));
        answerList.add(new Answer("\\(\\frac{a}{b}\\)",false, task2));
        answerList.add(new Answer("\\(\\frac{a+b}{b}\\)",false, task2));
        answerList.add(new Answer("\\(\\sqrt{a \\cdot b}\\)",false, task2));
        answerList.add(new Answer("\\(\\sqrt[a]{b}\\)",false, task2));

        answerList.add(new Answer("\\(a + b\\)",true, task2));
        answerList.add(new Answer("\\(a \\cdot b\\)",true, task2));
        answerList.add(new Answer("\\(\\sqrt[a]{b^a}\\)",true, task2));

        answerRepo.saveAll(answerList);
    }
    private void createAnswerTask3(){

        Task task = testEntityManager.find(Task.class, 3);
        List<Answer> answerList = new ArrayList<>();

        //   \(    \)
        answerList.add(new Answer("\\(@@\\)",true,"1;Q+", task));
        answerList.add(new Answer("\\(@@\\)",true,"1;Q-", task));
        answerList.add(new Answer("\\(@@\\)",true,"1;FPN-", task));
        answerList.add(new Answer("\\(@@\\)",true,"1;FPN+", task));
        answerList.add(new Answer("\\(@@.\\dot@@\\)",true,"2;Z;N", task));
        answerList.add(new Answer("\\(\\sqrt{@@}\\)",true,"1;SQRT=N", task));

        answerList.add(new Answer("\\(\\frac{\\pi}{@@}\\)",false,"1;N", task));
        answerList.add(new Answer("\\(\\sqrt{@@}\\)",false,"1;SQRT=R+", task));
        answerList.add(new Answer("\\(\\sqrt{-@@}\\)",false,"1;N", task));

        answerRepo.saveAll(answerList);
    }
    private void createAnswerTask4(){
        //answerList.add(new Answer("",false, task));
        //answerList.add(new Answer("",true, task));
        Task task = testEntityManager.find(Task.class, 4);
        List<Answer> answerList = new ArrayList<>();

        answerList.add(new Answer("Die Zahl \\(@@\\) liegt in \\(\\mathbb Z \\), aber nicht in \\(\\mathbb N \\)",false,"1;Q+", task));
        answerList.add(new Answer("Die Zahl \\(@@\\) liegt in \\(\\mathbb Z \\), aber nicht in \\(\\mathbb N \\)",false,"1;Q-", task));
        answerList.add(new Answer("Die Zahl \\(@@.\\dot@@\\) liegt in \\(\\mathbb R \\), aber nicht in \\(\\mathbb Q \\)",false,"2;Z;N", task));
        answerList.add(new Answer("Die Zahl \\(\\pi\\) liegt nicht in \\(\\mathbb R \\)",false, task));



        answerList.add(new Answer("Die Zahl \\(\\sqrt{-@@}\\) liegt in \\(\\mathbb C \\)",true,"1;N", task));
        answerList.add(new Answer("Die Zahl \\(\\pi\\) liegt in \\(\\mathbb R \\)",true, task));


        answerRepo.saveAll(answerList);

    }
    private void createAnswerTask5(){
        //answerList.add(new Answer("",false, task));
        //answerList.add(new Answer("",true, task));
        Task task = testEntityManager.find(Task.class, 5);
        List<Answer> answerList = new ArrayList<>();

        answerList.add(new Answer("Jede reelle Zahl ist eine rationale Zahl",false, task));
        answerList.add(new Answer("Jede reelle Zahl ist eine ganze Zahl",false, task));
        answerList.add(new Answer("Jede reelle Zahl ist eine natürliche Zahl",false, task));
        answerList.add(new Answer("Jede rationale Zahl ist eine ganze Zahl",false, task));
        answerList.add(new Answer("Jede rationale Zahl ist eine natürliche Zahl",false, task));
        answerList.add(new Answer("Jede ganze Zahl ist eine natürliche Zahl",false, task));
        answerList.add(new Answer("Jede komplexe Zahl ist eine reelle Zahl",false, task));

        answerList.add(new Answer("Jede natürliche Zahl ist eine ganze Zahl",true, task));
        answerList.add(new Answer("Jede natürliche Zahl ist eine rationale Zahl",true, task));
        answerList.add(new Answer("Jede natürliche Zahl ist eine reelle Zahl",true, task));
        answerList.add(new Answer("Jede ganze Zahl ist eine rationale Zahl",true, task));
        answerList.add(new Answer("Jede ganze Zahl ist eine reelle Zahl",true, task));
        answerList.add(new Answer("Jede rationale Zahl ist eine reelle Zahl",true, task));
        answerList.add(new Answer("Jede reelle Zahl ist eine komplexe Zahl",true, task));

        answerRepo.saveAll(answerList);

    }







}
