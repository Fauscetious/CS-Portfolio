import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet
{    
	private Digraph G;
	private SAP S;
	private HashMap<String, HashSet<Integer>> N;
	private HashMap<Integer, String> Sy;
	
    public WordNet(String synsets, String hypernyms)
    {
    	if(synsets == null || hypernyms == null){
    		throw new NullPointerException();
    	}
    	// TODO:  You may use the code below to open and parse the
    	// synsets and hypernyms file.  However, you MUST add your
    	// own code to actually store the file contents into the
    	// data structures you create as fields of the WordNet class.
    	N = new HashMap<String, HashSet<Integer>>();
    	Sy = new HashMap<Integer, String>();
        // Parse synsets
        int largestId = -1;				// TODO: You might find this value useful 
        In inSynsets = new In(synsets);
        while (inSynsets.hasNextLine())
        {
            String line = inSynsets.readLine();
            String[] tokens = line.split(",");
            
            // Synset ID
            int id = Integer.parseInt(tokens[0]);
            if (id > largestId)
            {
                largestId = id;
            }

            // Nouns in synset
            String synset = tokens[1];
            String[] nouns = synset.split(" ");
            for (String noun : nouns)
            {
               // TODO: you should probably do something here
            	if(!N.containsKey(noun)){
            		N.put(noun, new HashSet<Integer>());
            	}
        		N.get(noun).add(id);
        		if(!Sy.containsKey(id)){
        			Sy.put(id, synset);
        		}
            }
            
            // tokens[2] is gloss, but we're not using that
        }
        inSynsets.close();
        G = new Digraph(largestId+1);
        // Parse hypernyms
        In inHypernyms = new In(hypernyms);
        while (inHypernyms.hasNextLine())
        {
            String line = inHypernyms.readLine();
            String[] tokens = line.split(",");
            
            int v = Integer.parseInt(tokens[0]);
            
            for (int i=1; i < tokens.length; i++)
            {
               G.addEdge(v, Integer.parseInt(tokens[i]));
            }
        }
        inHypernyms.close();
        
        Topological T = new Topological(G);
        Topological R = new Topological(G.reverse());
        if(!T.hasOrder()||!R.hasOrder()){
        	throw new IllegalArgumentException();
        }
        S = new SAP(G);
    }

    public Iterable<String> nouns()
    {
    	return N.keySet();
    }

    public boolean isNoun(String word)
    {
    	if(word == null){
    		throw new NullPointerException();
    	}
    	return N.containsKey(word);
    }

    public int distance(String nounA, String nounB)
    {
    	if(nounA == null || nounB == null){
    		throw new NullPointerException();
    	}
    	if(!isNoun(nounA)||!isNoun(nounB)){
    		throw new IllegalArgumentException();
    	}
    	Set<Integer> aSet = N.get(nounA);
    	Set<Integer> bSet = N.get(nounB);
    	return S.length(aSet, bSet);
    }

    public String sap(String nounA, String nounB)
    {
    	if(nounA == null || nounB == null){
    		throw new NullPointerException();
    	}
    	if(!isNoun(nounA)||!isNoun(nounB)){
    		throw new IllegalArgumentException();
    	}
    	Set<Integer> aSet = N.get(nounA);
    	Set<Integer> bSet = N.get(nounB);
    	return Sy.get(S.ancestor(aSet, bSet));
    }
    
    private void testNouns(String nounA, String nounB)
    {
    	if(nounA == null || nounB == null){
    		throw new NullPointerException();
    	}
        System.out.print("'" + nounA + "' and '" + nounB + "': ");
        System.out.print("sap: '" + sap(nounA, nounB));
        System.out.println("', distance=" + distance(nounA, nounB));
    }

    // for unit testing of this class
    public static void main(String[] args)
    {
		String synsetsFile = "testInput/synsets.txt";
		String hypernymsFile = "testInput/hypernyms.txt";

		WordNet wordnet = new WordNet(synsetsFile, hypernymsFile);
        wordnet.testNouns("municipality", "region");
        wordnet.testNouns("individual", "edible_fruit");
        wordnet.testNouns("Black_Plague", "black_marlin");
        wordnet.testNouns("American_water_spaniel", "histology");
        wordnet.testNouns("Brown_Swiss", "barrel_roll");
        
        wordnet.testNouns("chocolate", "brownie");
        wordnet.testNouns("cookie", "brownie");
        wordnet.testNouns("martini", "beer");
    }
}