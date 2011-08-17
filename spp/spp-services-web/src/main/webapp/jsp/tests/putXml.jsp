<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ page import = "java.io.File"%>
<%@ page import = "java.io.FileInputStream"%>
<%@ page import = "java.sql.Connection"%>
<%@ page import = "java.sql.PreparedStatement"%>
<%@ page import = "javax.naming.InitialContext"%>
<%@ page import = "javax.sql.DataSource"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%
File file;


file = new File("D:/Eclipse/eclipse/rules_GENERIC_ALL_PARTNERS.xml");
try{
	InitialContext ctx = new InitialContext();
	
	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
    //lien vers la base de données
    Connection connection = ds.getConnection();
    
    //lien vers notre fichier image
    FileInputStream stream = new FileInputStream(file);

    //préparation de l'instruction SQL
    String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'GENERIC_ALL_PARTNERS'";
    PreparedStatement statement = connection.prepareStatement(sql);

    //insertion du xml
    statement.setBinaryStream(1, stream, (int)file.length());
    statement.executeUpdate();
	connection.close();

  }catch(Exception e){
	  out.println("XXXXXXXXXXXXXXX");
	  e.printStackTrace();
     //traitement des erreurs SQL, IO, etc .
  }finally {
     //fermeture de la connexion, du flux, etc.
  }

  
  file = new File("D:/Eclipse/eclipse/rules_GENERIC_NETHERLANDS_PARTNERS.xml");
  try{
  	InitialContext ctx = new InitialContext();
  	
  	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
      //lien vers la base de données
      Connection connection = ds.getConnection();
      
      //lien vers notre fichier image
      FileInputStream stream = new FileInputStream(file);

      //préparation de l'instruction SQL
      String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'GENERIC_NETHERLANDS_PARTNERS'";
      PreparedStatement statement = connection.prepareStatement(sql);

      //insertion du xml
      statement.setBinaryStream(1, stream, (int)file.length());
      statement.executeUpdate();
  	connection.close();

    }catch(Exception e){
  	  out.println("XXXXXXXXXXXXXXX");
  	  e.printStackTrace();
       //traitement des erreurs SQL, IO, etc .
    }finally {
       //fermeture de la connexion, du flux, etc.
    }  
    
    
    file = new File("D:/Eclipse/eclipse/rules_PROFILE_BID_GUIDE_SWEDEN.xml");
    try{
    	InitialContext ctx = new InitialContext();
    	
    	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
        //lien vers la base de données
        Connection connection = ds.getConnection();
        
        //lien vers notre fichier image
        FileInputStream stream = new FileInputStream(file);

        //préparation de l'instruction SQL
        String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'PROFILE_BID_GUIDE_SWEDEN'";
        PreparedStatement statement = connection.prepareStatement(sql);

        //insertion du xml
        statement.setBinaryStream(1, stream, (int)file.length());
        statement.executeUpdate();
    	connection.close();

      }catch(Exception e){
    	  out.println("XXXXXXXXXXXXXXX");
    	  e.printStackTrace();
         //traitement des erreurs SQL, IO, etc .
      }finally {
         //fermeture de la connexion, du flux, etc.
      }  
  
      file = new File("D:/Eclipse/eclipse/rules_PROFILE_PRODUCT_BROWSER_STORAGE.xml");
      try{
      	InitialContext ctx = new InitialContext();
      	
      	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
          //lien vers la base de données
          Connection connection = ds.getConnection();
          
          //lien vers notre fichier image
          FileInputStream stream = new FileInputStream(file);

          //préparation de l'instruction SQL
          String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'PROFILE_PRODUCT_BROWSER_STORAGE'";
          PreparedStatement statement = connection.prepareStatement(sql);

          //insertion du xml
          statement.setBinaryStream(1, stream, (int)file.length());
          statement.executeUpdate();
      	connection.close();

        }catch(Exception e){
      	  out.println("XXXXXXXXXXXXXXX");
      	  e.printStackTrace();
           //traitement des erreurs SQL, IO, etc .
        }finally {
           //fermeture de la connexion, du flux, etc.
        }
        
        file = new File("D:/Eclipse/eclipse/rules_PROFILE_PRODUCT_BROWSER_SUPPLIES.xml");
        try{
        	InitialContext ctx = new InitialContext();
        	
        	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
            //lien vers la base de données
            Connection connection = ds.getConnection();
            
            //lien vers notre fichier image
            FileInputStream stream = new FileInputStream(file);

            //préparation de l'instruction SQL
            String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'PROFILE_PRODUCT_BROWSER_SUPPLIES'";
            PreparedStatement statement = connection.prepareStatement(sql);

            //insertion du xml
            statement.setBinaryStream(1, stream, (int)file.length());
            statement.executeUpdate();
        	connection.close();

          }catch(Exception e){
        	  out.println("XXXXXXXXXXXXXXX");
        	  e.printStackTrace();
             //traitement des erreurs SQL, IO, etc .
          }finally {
             //fermeture de la connexion, du flux, etc.
          }
  
		
          file = new File("D:/Eclipse/eclipse/rules_PROFILE_PRODUCT_BROWSER_PC_MONITOR_PROCURVE_SERVERS.xml");
          try{
          	InitialContext ctx = new InitialContext();
          	
          	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
              //lien vers la base de données
              Connection connection = ds.getConnection();
              
              //lien vers notre fichier image
              FileInputStream stream = new FileInputStream(file);

              //préparation de l'instruction SQL
              String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'PROFILE_PRODUCT_BROWSER_PC_MONITOR_PROCURVE_SERVERS'";
              PreparedStatement statement = connection.prepareStatement(sql);

              //insertion du xml
              statement.setBinaryStream(1, stream, (int)file.length());
              statement.executeUpdate();
          	connection.close();

            }catch(Exception e){
          	  out.println("XXXXXXXXXXXXXXX");
          	  e.printStackTrace();
               //traitement des erreurs SQL, IO, etc .
            }finally {
               //fermeture de la connexion, du flux, etc.
            }
            
            file = new File("D:/Eclipse/eclipse/rules_GENERIC_NO_SUPPLIES_COMPETITOR.xml");
            try{
            	InitialContext ctx = new InitialContext();
            	
            	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
                //lien vers la base de données
                Connection connection = ds.getConnection();
                
                //lien vers notre fichier image
                FileInputStream stream = new FileInputStream(file);

                //préparation de l'instruction SQL
                String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'GENERIC_NO_SUPPLIES_COMPETITOR'";
                PreparedStatement statement = connection.prepareStatement(sql);

                //insertion du xml
                statement.setBinaryStream(1, stream, (int)file.length());
                statement.executeUpdate();
            	connection.close();

              }catch(Exception e){
            	  out.println("XXXXXXXXXXXXXXX");
            	  e.printStackTrace();
                 //traitement des erreurs SQL, IO, etc .
              }finally {
                 //fermeture de la connexion, du flux, etc.
              }
              
              file = new File("D:/Eclipse/eclipse/rules_PROFILE_EASY_CHANNEL_REGISTERED.xml");
              try{
              	InitialContext ctx = new InitialContext();
              	
              	DataSource ds = (DataSource)ctx.lookup("jdbc/SPP_PORTALDS");
                  //lien vers la base de données
                  Connection connection = ds.getConnection();
                  
                  //lien vers notre fichier image
                  FileInputStream stream = new FileInputStream(file);

                  //préparation de l'instruction SQL
                  String sql = "UPDATE SPP_GROUP SET RULES=? WHERE name like 'PROFILE_EASY_CHANNEL_REGISTERED'";
                  PreparedStatement statement = connection.prepareStatement(sql);

                  //insertion du xml
                  statement.setBinaryStream(1, stream, (int)file.length());
                  statement.executeUpdate();
              	connection.close();

                }catch(Exception e){
              	  out.println("XXXXXXXXXXXXXXX");
              	  e.printStackTrace();
                   //traitement des erreurs SQL, IO, etc .
                }finally {
                   //fermeture de la connexion, du flux, etc.
                }
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>