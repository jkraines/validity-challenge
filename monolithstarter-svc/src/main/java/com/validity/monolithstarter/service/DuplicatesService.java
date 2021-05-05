package com.validity.monolithstarter.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.io.IOException;

@Service
public class DuplicatesService {

    // this is the path for the .csv file the program reads from
    String recordFilePath = "../test-files/normal.csv";
    float similarity = (float) 0.650;

    /*
     This function uses memoization to determine the number of changes required to make 2 strings equivalent,
     the resulting number is known as the levenshtein distance. A change can be one of three things;
        1. swapping a character in a string with the character at the same position in the other string
        2. deleting a character in a string
        3. inserting a character in a string
     In order to find the cheapest overall solution, we calculate the cost of making each of these changes on the first
     character of a string in order to make it equal to the first character in the other string, and then we repeat this
     process for each subsequent character. Each time we calculate the cost of a subsequent character, we are computing
     a subproblem of the overall problem. In order to avoid computing the solution to the same subproblems over and over
     again, we store the solution to each subproblem in a 2d array.
     This solution has a complexity of O(m*n)
     Derived from a solution found at baeldung.com/java-levenshtein-distance
    */
    public int levenshteinMemoization(String record1, String record2){
        // the cost of each subproblem is placed in here, so we only compute each value once
        int subProblemCosts[][] = new int[record1.length()+1][record2.length()+1];

        for (int i = 0; i <= record1.length(); i++){
            for (int j = 0; j <= record2.length(); j++){
                if (i == 0){
                    subProblemCosts[i][j] = j;
                } else if (j == 0){
                    subProblemCosts[i][j] = i;
                } else {
                    // cost of each subproblem is the minimum cost of the 3 possible operations at that point
                    subProblemCosts[i][j] = Math.min(
                            // if the characters compared are different, we add one to the number of changes,
                            subProblemCosts[i-1][j-1] + (record1.charAt(i-1) == record2.charAt(j-1) ? 0 : 1),
                            Math.min(
                                    // an addition or deletion is the cost of all prior moves + 1
                                    subProblemCosts[i-1][j] + 1,
                                    subProblemCosts[i][j-1] + 1
                            )
                    );
                }
            }
        }
        // last final cost in our matrix is the levenshtein distance
        return subProblemCosts[record1.length()][record2.length()];
    }

    /*
      Reads each entry of the csv file into a list, then returns that list
     */
    public LinkedList<String> readCSV(){
        LinkedList<String> records = new LinkedList<>();
        // placed file reading in try/catch block to prevent untimely crash on an exception
        try {
            File recordFile = new File(recordFilePath);
            System.out.println(recordFile.getCanonicalPath());
            FileReader fileReader = new FileReader(recordFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            // this loop will read a line from the file and put it into the list until the end of the file is reached
            String line = bufferedReader.readLine();
            // read in the 2nd line since line 1 includes no valuable information
            line = bufferedReader.readLine();
            while(line != null){
                records.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException ioException){
            ioException.printStackTrace();
        }
        return records;
    }

    /*
      This function normalizes the levenshtein distance to more accurately illustrate the difference between the two
      strings. We do this by dividing the levenshtein distance by the maximum distance between the two strings, this
      yields a value on [0,1]. A result of 1 means the strings are identical, 0 means they are completely different.
     */
    public float normalizedLevenshtein(String record1, String record2){
        float levenshtein = (float)levenshteinMemoization(record1, record2);
        // the maximum difference is equal to the length of the longest string
        float maxDifference = Math.max(record1.length(), record2.length());
        return (maxDifference - levenshtein ) / maxDifference;
    }

    public String getDuplicatesMessage(){
        // get contents of csv file in list form
        LinkedList<String> records = readCSV();
        // compare each record with the others and store sets of duplicates in an array
        // if our list of records is {tony stark, tony stank, clark kent}...
        // this will end up as { {tony stark, tony stank}, {already matched this record!}, {clark kent}}
        LinkedList<String>[] duplicates = new LinkedList[records.size()];
        LinkedList<String> duplicateSet;
        // we will use this item in our array when an object has previously been discovered as a match.
        // the purpose of this is to avoid having matched records be listed as a match later.
        LinkedList<String> alreadyMatchedRecord = new LinkedList<>();
        alreadyMatchedRecord.add("already matched this record!");
        for(int i = 0; i < records.size(); i++){
            duplicateSet = new LinkedList<>();
            duplicateSet.add(records.get(i));
            // if we've already matched this record, skip to the next one
            if (duplicates[i] != null) {
                if (duplicates[i].equals(alreadyMatchedRecord)) {
                    continue;
                }
            }
            for (int j = 0; j < records.size(); j++){
                            // skip the comparison if we are comparing a record with itself
                if (i == j){
                    continue;
                }
                // if we've already matched this record, skip to the next one
                if (duplicates[j] != null) {
                    if (duplicates[j].equals(alreadyMatchedRecord)) {
                        continue;
                            }
                    }
                    // get the normalized levenshtein distance between the two records
                    float distance = normalizedLevenshtein(records.get(i), records.get(j));
                    // if the normalized distance is above a certain value, we count the pair as duplicates.
                    // I arbitrarily chose the magic number to be 0.650...this should be fine tuned
                    if (distance >= similarity){
                        // add the 2nd record to record1's list of matching records
                        duplicateSet.add(records.get(j));
                        // indicate that the 2nd record has been matched, so we don't waste time checking it again
                        duplicates[j] = alreadyMatchedRecord;
                        }
                    }
            duplicates[i] = duplicateSet;
        }

        return jsonify(duplicates);
    }

    /*
      I don't think java has native JSON support, so I made my own methods for converting the data into javascript
     */
    public String jsonify(LinkedList<String>[] sortedRecords){
        LinkedList<String[]> matchingRecords = new LinkedList<>();
        LinkedList<String> uniqueRecords = new LinkedList<>();
        // sort through the array where we combined duplicates into lists and removed their later references to seperate duplicates and nonduplicates
        for(LinkedList<String> dupeSet : sortedRecords){
            // see if this is a record which we removed and put into a list
            if (dupeSet.contains("already matched this record!")){
                continue;
            }
            // if this is a lone record, put it into the list of nonduplicates
            if (dupeSet.size() == 1){
                uniqueRecords.add(dupeSet.get(0));
            }
            // if this is a list of duplicates, convert to an array and place it into the list of duplicates
            if (dupeSet.size() > 1){
                String[] dupes = new String[dupeSet.size()];
                for (int i = 0; i < dupeSet.size(); i++){
                    dupes[i] = dupeSet.get(i);
                }
                matchingRecords.add(dupes);
            }

        }
        // convert list of nonduplicates into an array
        String[] uniqueRecordsArr = new String[uniqueRecords.size()];
        for (int i = 0; i < uniqueRecords.size(); i++){
            uniqueRecordsArr[i] = uniqueRecords.get(i);
        }
        // format our two collections as json
        String json = "{ \"duplicates\": ";
        String[] matchingJsonArrs = new String[matchingRecords.size()];
        for (int i = 0; i < matchingRecords.size(); i++){
            matchingJsonArrs[i] = arrToJsonString(matchingRecords.get(i));
        }
        json += arr2dtoJsonString(matchingJsonArrs);
        json += ", \"nonduplicates\": ";
        json += arrToJsonString(uniqueRecordsArr);
        json += "}";
        return json;
    }

    /*
      formats an array of strings as a json array
    */
    public String arrToJsonString(String[] arr){
        String arrStr = "[";
        for( int i = 0; i < arr.length; i++){

            // a lone " will break the json, so this block will throw an escape character in front of each " in the data
            for (int j = 0; j < arr[i].length(); j++){
                if (arr[i].charAt(j) == '\"'){
                    if (arr[i].charAt(j-1) != '\\') {
                        arr[i] = arr[i].substring(0, j) + "\\" + arr[i].substring(j);
                    }
                }
            }

            if (i == 0){
                arrStr += "\""+ arr[i] + "\"";
            } else {
                arrStr += ", \"" + arr[i] + "\"";
            }
        }
        arrStr += "]";
        return arrStr;
    }

    /*
      formats an array of json arrays as a json array
     */
    public String arr2dtoJsonString(String[] arr){
        String arrStr = "[";
        for( int i = 0; i < arr.length; i++){
            if (i == 0){
                arrStr +=  arr[i];
            } else {
                arrStr += ", " + arr[i];
            }
        }
        arrStr += "]";
        return arrStr;
    }
}