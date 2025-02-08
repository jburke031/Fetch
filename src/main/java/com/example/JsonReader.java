package com.example;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class JsonReader {
    public static void main(String[] args) throws IOException, InterruptedException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("C:\\Users\\owner\\Fetch\\takehomeassignment\\Example2.json"));   // Enter JSON path

        String retailer = jsonNode.get("retailer").asText();
        String purchaseDate = jsonNode.get("purchaseDate").asText();
        String purchaseTime = jsonNode.get("purchaseTime").asText();
        double total = jsonNode.get("total").asDouble();

        JsonNode items = jsonNode.get("items");
        List<String> shortDescription = new ArrayList<>();
        List<Double> price = new ArrayList<>();
        int checkDay = 0;
        int checkHour = 0;
        int checkMin = 0;
        int points = 0;

        if(items.isArray())
        {
            for(JsonNode item: items){
                shortDescription.add(item.get("shortDescription").asText().trim());
                price.add(item.get("price").asDouble());
            }
        }

       // CHECKING DAY/* 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(purchaseDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            checkDay = calendar.get(Calendar.DATE);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          //  */
          //CHECK TIME - HOUR
          SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
          try {
            Date time = timeFormat.parse(purchaseTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            checkHour = calendar.get(Calendar.HOUR_OF_DAY);
            checkMin = calendar.get(Calendar.MINUTE);
       
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        System.out.println("Retailer: " + retailer);
       System.out.println("Purchase date: " + purchaseDate);
       System.out.println("Purchase time: " + purchaseTime);
       points = calculateRoundDollarAmount(total, points);
       points = calculateMultiple(total, points);
       points = calculateItems(shortDescription,points);
       points = calculateRetailer(retailer,points);
       points = calculateTrim(shortDescription,points,price);
       points = oddPurchaseDate(points, checkDay);
       points = timeOfPurchase(checkMin, points, checkHour);
       System.out.println("Total Points: " + points);

       String response = returnID();
       System.out.println("Generated ID: " + response);
       String id = createID();

       GetPoints.main(id);        // Using API Call in GetPoints class to respond to UUID and output points
 
    }

    public static String createID()
    {
        return UUID.randomUUID().toString();
    }
    public static String returnID() 
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String receiptID = createID();
            Map<String, Object> responseJson = Map.of("id", receiptID);
            return objectMapper.writeValueAsString(responseJson);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return "{}";
        }
    }


    public static int calculateRoundDollarAmount(double total, int cache)
    {
        if(total % 1 == 0)
        {
            return cache += 50;
        }
        else
        {
            return cache += 0;
        }

    }

    public static int calculateMultiple(double total, int cache)
    {
        total = total *100;
        if(total % 25 == 0)
        {
            return cache += 25;
        }
        else
        {
            return cache +=0;
        }
    }

    public static int calculateRetailer(String retailer, int cache)
    {
        int count = 0;
        retailer = retailer.replaceAll("[^a-zA-Z0-9]", "");

        String regex = " ";
        String [] splitArray = retailer.split(regex);
        for(String s : splitArray)
        {
            count += s.length();
        }
        return cache += count;
    }

    public static int calculateItems(List<String> shortDescription, int cache)
    {
        int receiptItems = shortDescription.size() / 2;
        return cache += receiptItems * 5;        

    }

    public static int calculateTrim(List<String> shortDescription, int cache, List<Double> price)
    {
        double x = 0;
        ArrayList<Integer> charDescription = new ArrayList<>();

        for (String string : shortDescription)
        {
            charDescription.add(string.length());   
        }

        for (int i = 0; i < charDescription.size(); i++)
        {
            int element = charDescription.get(i);
            if(element % 3 == 0)
            {
                double p = price.get(i);
                x = Math.ceil(p * 0.2);
                cache += x;
            }
        }
        return cache;

    }

    public static int oddPurchaseDate(int cache, int checkDay)
    {
        if(checkDay % 2 != 0)
        {
            return cache += 6;
        }
        return cache;

    }

    public static int timeOfPurchase(int checkMin, int cache, int checkHour)
    {
        if(checkHour >= 14 && checkHour < 16)
        {
            if(checkHour == 14 && checkMin == 0)
            {
                return cache;
            }

            return cache += 10;
        }
        return cache;

    }

}