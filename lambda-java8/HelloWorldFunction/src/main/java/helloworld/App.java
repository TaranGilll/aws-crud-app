package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.dynamodbv2.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;


public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
  private static final Gson GSON = new Gson();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
      final Map<String, String> headersMap = new HashMap<>();
      headersMap.put("Content-Type", ContentType.APPLICATION_JSON.toString());
      headersMap.put("X-Custom-Header", ContentType.APPLICATION_JSON.toString());
      headersMap.put("X-Requested-With", "*");
      headersMap.put("Access-Control-Allow-Origin", "*");
      headersMap.put("Access-Control-Allow-Credentials", "true");
      headersMap.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
      headersMap.put("Access-Control-Expose-Headers", "date");
      headersMap.put("Access-Control-Allow-Headers",
      "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

      APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withHeaders(headersMap);
      AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
      DynamoDBMapper mapper = new DynamoDBMapper(client);
     
      // POST
      if(input.getHttpMethod().equalsIgnoreCase(HttpMethod.POST.name())) {
        if(input.getPath().equalsIgnoreCase("/students")) {
          JsonObject inputJson = Optional.ofNullable(input.getBody())
                .filter(s -> s.length() > 0)
                .map(JsonParser::parseString)
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

          Item item = new Gson().fromJson(inputJson, Item.class);
          String body = input.getBody();
          String studentID = UUID.randomUUID().toString();
          String firstName = body.substring(body.indexOf(":")+2, body.indexOf(" "));
          String lastName = body.substring(body.indexOf(" ")+1, body.length()-2);
    
          item.setStdType("student");
          item.setStudentID(studentID);
          item.setFirstName(firstName);
          item.setLastName(lastName);
          
          mapper.save(item);
          JsonObject result = new JsonObject();
          result.add("student", JsonParser.parseString(GSON.toJson(item)));  
          response.withStatusCode(HttpStatus.SC_OK).withBody(GSON.toJson(result));
        }
      }

      //UPDATE
      else if(input.getHttpMethod().equalsIgnoreCase(HttpMethod.PUT.name())) {
        if(input.getPath().equalsIgnoreCase("/students")) {
          JsonObject inputJson = Optional.ofNullable(input.getBody())
                .filter(s -> s.length() > 0)
                .map(JsonParser::parseString)
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

          Item item = new Gson().fromJson(inputJson, Item.class);
          String body = input.getBody();
          String studentID = body.substring(body.indexOf(":")+2, body.indexOf(" "));
          String firstName = body.substring(body.indexOf(" ")+1, body.lastIndexOf(" "));
          String lastName = body.substring(body.lastIndexOf(" ")+1, body.length()-2);
    
          item.setStdType("student");
          item.setStudentID(studentID);
          item.setFirstName(firstName);
          item.setLastName(lastName);
          
          mapper.save(item);
          response.withStatusCode(200).withBody("Update");
        }
      }

      // READ
      else if(input.getHttpMethod().equalsIgnoreCase(HttpMethod.GET.name())) {
        if(input.getPath().equalsIgnoreCase("/students")) {
          
          HashMap<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
          eav.put(":stdType", new AttributeValue().withS("student"));
          
          DynamoDBQueryExpression<Item> expression = new DynamoDBQueryExpression<Item>()
          .withKeyConditionExpression("stdType = :stdType")
          .withExpressionAttributeValues(eav);

          List<Item> students = mapper.query(Item.class, expression);

          JsonObject result = new JsonObject();
          result.add("students", JsonParser.parseString(GSON.toJson(students)));  
          response.withStatusCode(HttpStatus.SC_OK).withBody(GSON.toJson(result));
        }
      }


      // DELETE
      else if(input.getHttpMethod().equalsIgnoreCase(HttpMethod.DELETE.name())) {
        if(input.getPath().equalsIgnoreCase("/students")) {
          
          JsonObject inputJson = Optional.ofNullable(input.getBody())
                .filter(s -> s.length() > 0)
                .map(JsonParser::parseString)
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .orElse(null);

          Item item = new Gson().fromJson(inputJson, Item.class);
          String body = input.getBody();
          String studentID = body.substring(body.indexOf(":")+2, body.length()-2);
    
          item.setStdType("student");
          item.setStudentID(studentID);
          
          mapper.delete(item);

          JsonObject returnObject = new JsonObject();
          returnObject.addProperty("Delete", Boolean.TRUE);
          response.withStatusCode(HttpStatus.SC_OK).withBody(returnObject.toString());
        }
      }

      return response;
  }
}