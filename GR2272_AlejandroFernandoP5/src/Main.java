import features.Feature;
import featurizers.PersonFeaturizer;
import featurizers.WeatherFeaturizer;
import labellers.ShouldPlayTenisToday;
import models.Dataset;
import models.LabeledDataset;
import models.Person;
import models.Weather;
import strategies.EntropyStrategy;
import strategies.ErrorRateStrategy;
import strategies.GiniStrategy;
import trees.DecisionTree;
import trees.GreedyTreeLearner;
import visitors.ConsoleVisitor;
import visitors.GraphVizVisitor;
import visitors.StringVisitor;
import visitors.TreeVisitor;
import java.util.Arrays;
import java.util.Collections;

/**
 * Clase principal con demostraciones de todos los apartados de la practica.
 */
public class Main {
	/**
	 * Evita la instanciacion de la clase lanzadora. Para evitar un warning al hacer JavaDoc.
	 */
	private Main() {
	}

	/**
	 * Ejecuta las demos de los cuatro apartados.
	 *
	 * @param args argumentos de linea de comandos.
	 */
	public static void main(String[] args) {
		System.out.println("=== APARTADO 1: Dataset y Features ===\n");
		demoApartado1();
		
		System.out.println("\n=== APARTADO 2: GreedyTreeLearner ===\n");
		demoApartado2();
		
		System.out.println("\n=== APARTADO 3: Generando Predicados ===\n");
		demoApartado3();
		
		System.out.println("\n=== APARTADO 4: Visualización con Visitor Pattern ===\n");
		demoApartado4();
	}
	
	/**
	 * Demostracion del apartado 1 con datasets y features.
	 */
	public static void demoApartado1() {
		Dataset<Person> dataSet = buildDataSet();
		System.out.println("dataset: " + dataSet);

		dataSet.removeDuplicates();
		System.out.println("dataset w/o duplicates: " + dataSet);

		Feature<Integer> ages = dataSet.feature("age");
		System.out.println("Ages: " + ages);
		Collections.sort(ages);
		System.out.println("Ages sorted: " + ages);
		System.out.println("Min age: " + ages.min());
		System.out.println("Gender distribution: " + dataSet.feature("gender").distribution());
	}
	
	/**
	 * Demostracion del apartado 2 con aprendizaje greedy y varias estrategias.
	 */
	public static void demoApartado2() {
		LabeledDataset<Weather> weatherDataset = new LabeledDataset<>(
			new WeatherFeaturizer(), 
			new ShouldPlayTenisToday()
		);
		
		Weather[] conditions = {
			new Weather("sunny", 85, 85, false),
			new Weather("sunny", 80, 90, true),
			new Weather("overcast", 83, 86, false),
			new Weather("rainy", 70, 96, false),
			new Weather("rainy", 68, 80, false),
			new Weather("rainy", 65, 70, true),
			new Weather("overcast", 64, 65, true),
			new Weather("sunny", 72, 95, false),
			new Weather("sunny", 69, 70, false),
			new Weather("rainy", 75, 80, false),
		};
		
		weatherDataset.addAll(conditions);
		System.out.println("Weather Dataset: " + weatherDataset);
		System.out.println("Label Distribution: " + weatherDataset.getLabelDistribution());
		
		System.out.println("\n--- Usando Gini Strategy ---");
		GreedyTreeLearner<Weather> learnerGini = new GreedyTreeLearner<>(new GiniStrategy<>());
		DecisionTree<Weather> treeGini = learnerGini.learn(weatherDataset);
		System.out.println("Predicciones (Gini):");
		System.out.println(treeGini.predict(weatherDataset.items()));
		
		System.out.println("\n--- Usando Error Rate Strategy ---");
		GreedyTreeLearner<Weather> learnerError = new GreedyTreeLearner<>(new ErrorRateStrategy<>());
		DecisionTree<Weather> treeError = learnerError.learn(weatherDataset);
		System.out.println("Predicciones (ErrorRate):");
		System.out.println(treeError.predict(weatherDataset.items()));
		
		System.out.println("\n--- Usando Entropy Strategy ---");
		GreedyTreeLearner<Weather> learnerEntropy = new GreedyTreeLearner<>(new EntropyStrategy<>());
		DecisionTree<Weather> treeEntropy = learnerEntropy.learn(weatherDataset);
		System.out.println("Predicciones (Entropy):");
		System.out.println(treeEntropy.predict(weatherDataset.items()));
		
		System.out.println("\nPredicciones en nuevas instancias:");
		Weather[] newConditions = {
			new Weather("sunny", 90, 80, false),
			new Weather("rainy", 60, 85, true),
			new Weather("overcast", 75, 75, false)
		};
		System.out.println(treeGini.predict(Arrays.asList(newConditions)));
	}
	
	/**
	 * Demostracion del apartado 3 con generacion de predicados desde el arbol.
	 */
	public static void demoApartado3() {
		DecisionTree<Person> dt = buildPersonDecisionTree();
		
		System.out.println("Predicados generados a partir del árbol:");
		
		try {
			var oldMaleP = dt.getPredicate("old male");
			System.out.println("✓ Predicado 'old male' generado");
			
			var youngMaleP = dt.getPredicate("young male");
			System.out.println("✓ Predicado 'young male' generado");
			
			var femaleP = dt.getPredicate("female");
			System.out.println("✓ Predicado 'female' generado");
			
			Person[] people = {
				new Person("Pedro", 66, 75, 180, true),
				new Person("Luis", 34, 75, 176, true),
				new Person("Ana", 47, 54, 158, false),
			};
			
			System.out.println("\nTesting predicates:");
			for (Person p : people) {
				System.out.print(p.getName() + " (" + p.getAge() + ", " + (p.isMale() ? "M" : "F") + "): ");
				if (oldMaleP.test(p)) System.out.print("old male ");
				if (youngMaleP.test(p)) System.out.print("young male ");
				if (femaleP.test(p)) System.out.print("female ");
				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Crea un dataset de ejemplo para el apartado 1.
	 *
	 * @return dataset de personas con featurizer asociado.
	 */
	public static Dataset<Person> buildDataSet() {
		Person[] people = {
			new Person("Pedro", 66, 75, 180, true),
			new Person("Ana", 47, 54, 158, false),
			new Person("Luis", 34, 75, 176, true),
			new Person("Rosa", 47, 54, 158, false)
		};

		Dataset<Person> dataSet = new Dataset<>(new PersonFeaturizer());
		dataSet.addAll(people);
		return dataSet;
	}

	/**
	 * Construye un arbol de decision manual para el ejemplo de personas.
	 *
	 * @return arbol de decision preparado.
	 */
	public static DecisionTree<Person> buildPersonDecisionTree() {
		DecisionTree<Person> dt = new DecisionTree<>();
		dt.node("root")
			.withCondition("male", Person::isMale)
			.otherwise("female");

		dt.node("male")
			.withCondition("old male", person -> person.getAge() > 65)
			.withCondition("middle male", person -> person.getAge() <= 65 && person.getAge() > 34)
			.otherwise("young male");

		return dt;
	}
	
	/**
	 * Demostracion del apartado 4 con visitantes de visualizacion.
	 */
	public static void demoApartado4() {
		DecisionTree<Person> dt = buildPersonDecisionTree();
		
		System.out.println("--- Console Visitor ---");
		TreeVisitor<Person> consoleVisitor = new ConsoleVisitor<>();
		consoleVisitor.visitDecisionTree(dt);
		
		System.out.println("\n--- String Visitor ---");
		StringVisitor<Person> stringVisitor = new StringVisitor<>();
		String treeStructure = stringVisitor.getTreeAsString(dt);
		System.out.println("Tree Structure:");
		System.out.println(treeStructure);
		
		System.out.println("\n--- GraphViz DOT Format ---");
		TreeVisitor<Person> graphvizVisitor = new GraphVizVisitor<>();
		graphvizVisitor.visitDecisionTree(dt);
		
		System.out.println("\n\n--- Learned Tree Visualization ---");
		LabeledDataset<Weather> weatherDataset = new LabeledDataset<>(
			new WeatherFeaturizer(), 
			new ShouldPlayTenisToday()
		);
		
		Weather[] conditions = {
			new Weather("sunny", 85, 85, false),
			new Weather("sunny", 80, 90, true),
			new Weather("overcast", 83, 86, false),
			new Weather("rainy", 70, 96, false),
			new Weather("rainy", 68, 80, false),
			new Weather("rainy", 65, 70, true),
			new Weather("overcast", 64, 65, true),
			new Weather("sunny", 72, 95, false),
			new Weather("sunny", 69, 70, false),
			new Weather("rainy", 75, 80, false),
		};
		
		weatherDataset.addAll(conditions);
		
		GreedyTreeLearner<Weather> learner = new GreedyTreeLearner<>(new GiniStrategy<>());
		DecisionTree<Weather> learnedTree = learner.learn(weatherDataset);
		
		System.out.println("Learned Tree (String Format):");
		StringVisitor<Weather> weatherVisitor = new StringVisitor<>();
		System.out.println(weatherVisitor.getTreeAsString(learnedTree));
		
		System.out.println("Learned Tree (GraphViz DOT Format):");
		TreeVisitor<Weather> weatherGraphViz = new GraphVizVisitor<>();
		weatherGraphViz.visitDecisionTree(learnedTree);
	}
}
