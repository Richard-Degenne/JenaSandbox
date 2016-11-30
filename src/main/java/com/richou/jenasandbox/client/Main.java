package com.richou.jenasandbox.client;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import db.OntologyDB;
import java.util.Iterator;

/**
 *
 * @author richou
 */
public class Main {

    public static final String DB_URL = "jdbc:mysql://localhost/jena_sandbox";
    public static final String DB_USER = "root";
    public static final String DB_PASSWD = "rIchOU";
    public static final String DB = "MySQL";
    public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    public static final String FILE_NAME = "file:///E:\\Users\\richou\\src\\JenaSandbox\\resources\\sandbox.owl";
    public static final String ONTOLOGY_NAME = "sandbox";
    public static final String ONTOLOGY_URL = "http://www.semanticweb.org/richou/ontologies/2016/10/jena-sandbox#";
    

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String args[]) {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        IDBConnection connection = OntologyDB.connectDB(DB_URL, DB_USER, DB_PASSWD, DB);
        System.out.println("Connected to: " + connection);
        
        OntologyDB.createDBModelFromOntology(connection, ONTOLOGY_NAME, FILE_NAME);
        OntModel model = OntologyDB.getModelFromDB(connection, ONTOLOGY_NAME);
        
        printClasses(model);
        
        printInstances(model.getOntClass(ONTOLOGY_URL+"Expert"));
        
        System.out.println("sa march lol");
    }
    
    private static void printClasses(OntModel model) {
        for(Iterator i = model.listClasses(); i.hasNext(); ) {
        	OntClass c = (OntClass) i.next();
        	System.out.println(c.getLocalName());
	}
    }
    
    private static void printInstances(OntClass class_) {
        
        for(Iterator i = class_.listInstances() ; i.hasNext() ;) {
            Individual individual = (Individual) i.next();
            System.out.println(individual.getLocalName());
        }
    }
}
