import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.text.DecimalFormat;

class TreeNode {
    public static void main(String args[]) {
        TriesArticle companies = new TriesArticle();
        Tries tries=companies.inputFileName("companies.dat"); 
        System.out.println("Enter the article");
        Scanner sc = new Scanner(System.in);
        String newsArticle="";
        String thisLine="";
        while(true){
            thisLine=sc.nextLine();
            if(thisLine.matches("\\.*")){
               break;
            }
            newsArticle=newsArticle+" "+thisLine;   
        }

        newsArticle=newsArticle.replaceAll(" a ", " ");
        newsArticle=newsArticle.replaceAll(" an ", " ");
        newsArticle=newsArticle.replaceAll(" the ", " ");
        newsArticle=newsArticle.replaceAll(" and ", " ");
        newsArticle=newsArticle.replaceAll(" or ", " ");
        newsArticle=newsArticle.replaceAll(" but ", " ");
        companies.resultTable(newsArticle, tries);
    }
}

class TriesArticle {

    HashMap<String, String> companyDictionary = new HashMap<>();

    public Tries inputFileName(String filename) {

        File f = new File(filename);
        Tries tries = new Tries();
        Scanner in = null;
        try {
            in = new Scanner(f);
            int companyCount;
            while (in.hasNext()) {
                String a = in.nextLine();
                //System.out.println(a);
                String[] companyNames = a.split("\\t");
                for (int i = 0; i < companyNames.length; i++) {
                    //System.out.println(companyNames[i]);
                    companyNames[i]=companyNames[i].replaceAll("[\\p{P}\\p{S}]","");
                    //System.out.println(companyNames[i]);
                    tries.addWord(companyNames[i]);
                    companyDictionary.put(companyNames[i], companyNames[0]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return tries;
    }

    public void resultTable(String newsArticle, Tries companies) {

        newsArticle = newsArticle.replaceAll("[\\p{P}\\p{S}]","");
        String articleWords[] = newsArticle.split("\\s+");
        int wordCount = articleWords.length;
        HashMap<String, Integer> resultList = new HashMap<>();
        for (int i = 0; i < wordCount; i++) {
            boolean isCompany = companies.searchWord(articleWords[i]);

            if (isCompany) {
                String company = (String)companyDictionary.get(articleWords[i]);
                if (resultList.containsKey(company)) {
                    int hitCount = resultList.get(company);
                    hitCount++;
                    resultList.put(company,hitCount);
                }
                else{
                    resultList.put(company,1);
                }
            }

        }
        System.out.println(String.format("%10s", "Company") +"\t\t" + String.format("%9s", "Hit Count") +"\t\t" + String.format("%9s", "Relevance") );

        int valuecount=0;
        double relevancecount=0;
        wordCount=wordCount-1;
        DecimalFormat numberFormat=new DecimalFormat("#.0000");

        for (String CompanyName : resultList.keySet())
        {
            String variableKey = CompanyName;
            int variableValue = resultList.get(variableKey);
            valuecount=valuecount+variableValue;
            double relevance = (double)variableValue/wordCount;
            relevancecount=relevancecount+relevance;
            System.out.println( String.format("%10s", variableKey)  +"\t\t"+ String.format("%9s", variableValue) +"\t\t"+ String.format("%9s", numberFormat.format(relevance)) );
        }
        System.out.println(String.format("%10s", "Total") +"\t\t"+ String.format("%9s", valuecount) +"\t\t"+ String.format("%9s", numberFormat.format(relevancecount))) ;
        System.out.println( String.format("%10s", "Total Words") +"\t\t"+ String.format("%9s", wordCount) ); 

    }
}

class Tries {

    boolean isWord;
    Tries[] children;

    public Tries() {
        this.isWord = false;
        children = new Tries[52];
    }

    public void addWord(String word) {

        word=word.replaceAll(" ","");

        if (word.length() == 0) {
            this.isWord = true;
        } else {

            int treeNodeIndex = getIndex(word.charAt(0));

            if (this.children[treeNodeIndex] == null) {
                this.children[treeNodeIndex] = new Tries();
            }
            this.children[treeNodeIndex].addWord(word.substring(1));
        }
    }

    public int getIndex(char character) {

        char A = 'A', Z = 'Z', a = 'a';
        int treeNodeIndex = 0;

        if (character >= A && character <= Z)
            treeNodeIndex = character - A;

        else
            treeNodeIndex = character - a + 26;

        return treeNodeIndex;
    }

    public boolean searchWord(String word) {

        word=word.replaceAll(" ","");

        if (word.length() == 0) {
            return this.isWord;
        } else {

            int treeNodeIndex = getIndex(word.charAt(0));

            if (this.children[treeNodeIndex] == null) {
                return false;
            }
            return this.children[treeNodeIndex].searchWord(word.substring(1));
        }
    }

}