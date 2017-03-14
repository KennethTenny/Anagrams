package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = 3;

    private Random random = new Random();

    public ArrayList<String> wordList= new ArrayList<>();

    public HashSet<String> wordSet= new HashSet<>();
    public HashMap<String,ArrayList<String>> lettersToWord= new HashMap<>();
    public HashMap<Integer,ArrayList<String>> sizeToWords= new HashMap<>();



    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
        }

        int flag1=0,flag2=0;
        for(int i=0; i< wordList.size(); i++)  // Initialising lettersToWord hashmap
        {
            String word=wordList.get(i);
            String temp=sortLetters(word);

            if(lettersToWord.containsKey(temp))
            {
                ArrayList<String> str=lettersToWord.get(temp);
                str.add(word);
                lettersToWord.put(temp,str);

                flag1++;
            }
            else
            {
                ArrayList<String> str=new ArrayList<>();
                str.add(word);
                lettersToWord.put(temp,str);
                flag2++;
            }

        }

        Log.d("dsfvsd","number1" +flag1);
        Log.d("dsfvsd","number2" + flag2);

        for(int i=0; i< wordList.size(); i++)  // Shortened: Initialising sizeToWords hashmap
        {
            String word=wordList.get(i);
            ArrayList<String> str=new ArrayList<>();

            if(sizeToWords.containsKey(word.length()))
            {
                str=sizeToWords.get(word.length());
            }

            str.add(word);
            sizeToWords.put(word.length(),str);

        }


    }


    public boolean isGoodWord(String word, String base)
    {
        if(wordSet.contains(word) && !word.contains(base))   // base-checking==whether the base is coming as it is in word.
            return true;
        else return false;
    }


    public String sortLetters(String s){
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

//    public String sortLetters(String word)
//    {
//        char arr[]=word.toCharArray();
//        Arrays.sort(arr);
//        word= arr.toString();
//        return word;
//    }


    public ArrayList<String> getAnagrams(String targetWord)
    {
        ArrayList<String> result = new ArrayList<String>();

        String check= sortLetters(targetWord);


        for(int i=0; i< wordList.size(); i++ )
        {
            String word= wordList.get(i);
            String temp = sortLetters(word);

            if(check.equals(temp))
            { result.add(word);}

            //Log.e("fvfdvd","vfdsv"+result.size() + wordList.size());
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String key;
        for (char c = 'a'; c <= 'z'; c++){
            key = sortLetters(word + c);
            if (lettersToWord.containsKey(key)){
                //Log.d(LOG_TAG, key);
                result.addAll(lettersToWord.get(key));
            }
        }
        // delete bad words
        for (int i = 0; i < result.size(); i++){
            if (!isGoodWord(result.get(i), word)){
                result.remove(i);
                i--;
            }
        }
        return result;
    }




    public String pickGoodStarterWord() {
        ArrayList<String> wordArray = sizeToWords.get(Math.min(MAX_WORD_LENGTH, wordLength));

        int maxLen = wordArray.size();
        int sp = random.nextInt(maxLen), loopIndex;
        String key;
        for (loopIndex=sp; loopIndex < (maxLen+sp+1); loopIndex++){
            key = wordArray.get(loopIndex % maxLen);
            if (getAnagramsWithOneMoreLetter(key).size() >= MIN_NUM_ANAGRAMS){
                break;
            }
        }

        wordLength += 1;
        return wordArray.get(loopIndex % maxLen);
    }

}