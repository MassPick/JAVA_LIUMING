package cn.com.szgao.test;
import java.io.IOException;  
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;  
  
public class JacksonDemo {  
    public static void main(String[] args) throws ParseException, JsonParseException, JsonMappingException, IOException  {  
        String json = "{\"name\":\"小民\",\"age\":20,\"birthday\":844099200000,\"email\":\"xiaomin@sina.com\"}";  
          
        /** 
         * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。 
         */  
        ObjectMapper mapper = new ObjectMapper();  
        User user = mapper.readValue(json, User.class);  
        System.out.println(user.getName() +"  "+user.getBirthday() +" "+user.getEmail());  
    }  
}  



  class User {  
    private String name;  
    private Integer age;  
    private Date birthday;  
    private String email;  
      
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
      
    public Integer getAge() {  
        return age;  
    }  
    public void setAge(Integer age) {  
        this.age = age;  
    }  
      
    public Date getBirthday() {  
        return birthday;  
    }  
    public void setBirthday(Date birthday) {  
        this.birthday = birthday;  
    }  
      
    public String getEmail() {  
        return email;  
    }  
    public void setEmail(String email) {  
        this.email = email;  
    }  
}  