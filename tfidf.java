import java.io.*; 
import java.util.*; 

public class tfidf 
{

  Map<String, List<Integer>> index = new HashMap<>();
  Map<String, List<String>> dataset = new HashMap<>(); //Dataset of documents
  List<String> documents = new ArrayList<String>();
  
  // read and created index
  void create(File file) throws IOException 
  {
    String name = file.getName();
    documents.add(name);
    //docid++;
    
    BufferedReader reader = null;
    try 
    {
      reader = new BufferedReader(new FileReader(file));
      String line;
      List<String> temp = new ArrayList<String>() ; // word tokenize
      while((line = reader.readLine()) != null) 
      {
        String words[] = line.split("[^A-Za-z]"); // word tokenize
        for(String _word : words) 
        {
          String word = _word.toLowerCase();
          temp.add(word);
        }
      }
      dataset.put(name,temp);
    } 
    finally 
    {
      if(reader != null) 
      {
        reader.close();
      }
    }
  }
        
  double calculator(String word, String document)
  {
    return tf(dataset.get(document), word) * idf(word);
  }

  public double tf(List<String> doc, String term) 
  {
    double result = 0;
    for (String word : doc) 
    {
      if (term.equalsIgnoreCase(word))
        result++;
    }
    return result / doc.size();
  }


  public double idf(String term) 
  {
    double n = 0;

    for (String document:documents) 
    {
      List<String> doc = dataset.get(document); 
      for (String word : doc) 
      {
        if (term.equalsIgnoreCase(word)) 
        {
          n++;
          break;
        }
      }
    }

    return Math.log(documents.size()*1.0/ n);
  }

  public void querySearch(String _word) 
  {
      String word = _word.toLowerCase();
      List<Double> res = new ArrayList<Double>();

      for (String t : documents) 
      {
        System.out.println(t+ " TF-IDF Score: " + calculator(word,t));
      }
  }
  
  // solution
  public void start() 
  {
    Scanner reader = new Scanner(System.in);
    System.out.println("Enter input directory: ");
    String input = reader.nextLine();
    System.out.println("Enter the query word: ");
    String query = reader.nextLine();
    reader.close();
    
    try 
    {
      File files[] = new File(input).listFiles();
      System.out.println("Creating index.");
      
      for(File file : files) 
      {
        System.out.println("Processing: " + file);
        create(file); // create index
      }

      System.out.println("Displaying the TF-IDF Score");
      querySearch(query);
    } 
    catch(IOException ex) 
    {
      // error
      throw new IllegalStateException(ex.getMessage(), ex);
    } 
    finally 
    {
      reader.close();
    }
  }

  public static void main (String args[]) throws Exception
  {    
    tfidf tf = new tfidf(); 
    tf.start(); 
  }
}
