package com.richou.jenasandbox.client;

import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
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

//        for (Iterator i = model.listClasses(); i.hasNext();) {
//            OntClass class_ = (OntClass) i.next();
//            System.out.println(class_.getLocalName());
//            for (Iterator j = class_.listInstances(); j.hasNext();) {
//                Individual individual = (Individual) j.next();
//                System.out.println(individual.getLocalName());
//                printProperties(individual, class_);
//            }
//        }
        searchFor(model);

        System.out.println("sa march lol");
    }

    public static void getProperty(OntModel model) {
        /* get the Expert class */
        OntClass expert = model.getOntClass(ONTOLOGY_URL + "Expert");
        // print out the name of the Expert class
        System.out.println(expert.getLocalName());

        // get the instances of the Expert class
        Iterator it = expert.listInstances();
        // print out the instances of the Expert class
        while (it.hasNext()) {
            Individual oi = (Individual) it.next();
            System.out.println(oi.getLocalName());

            if (expert.hasDeclaredProperty(model.getOntProperty("Name"), false)) {
                System.out.println("Oui !");
            } else {
                System.out.println("Non !");
            }

            //get the properties of the instances of the Expert class
            Iterator ipp = expert.listDeclaredProperties(false);
            while (ipp.hasNext()) {
                OntProperty p = (OntProperty) ipp.next();
                //print out property name and its values
                System.out.println(p.getLocalName());

                for (Iterator ivv = oi.listPropertyValues(p); ivv.hasNext();) {
                    String valuename = ivv.next().toString();
                    System.out.println(valuename);
                }
            }
        }
    }

    private static void printClasses(OntModel model) {
        for (Iterator i = model.listClasses(); i.hasNext();) {
            OntClass c = (OntClass) i.next();
            System.out.println(c.getLocalName());
        }
    }

    private static void printInstances(OntClass class_) {
        for (Iterator i = class_.listInstances(); i.hasNext();) {
            Individual individual = (Individual) i.next();
            System.out.println(individual.getLocalName());
        }
    }

    private static void printProperties(Individual individual, OntClass class_) {
        for (Iterator i = class_.listDeclaredProperties(); i.hasNext();) {
            OntProperty property = (OntProperty) i.next();
            System.out.println(property.getLocalName());

            for (Iterator j = individual.listPropertyValues(property); j.hasNext();) {
                System.out.println("\t" + j.next().toString());
            }
        }
    }
    
    private static void searchFor(OntModel model) {
        String rule = "[rule1: (?expert "+ONTOLOGY_URL+"hasResearch" + " ?research) " +
                "(?research "+ONTOLOGY_URL+"hasSubject" + " ?subject) " +
                "-> (?expert "+ONTOLOGY_URL+"isFamiliarWith" + " ?subject)]";
        
        String queryString = "PREFIX JenaSandbox:<"+ONTOLOGY_URL+"> " +
                "SELECT ?expert ?subject " +
                "WHERE {?expert JenaSandbox:isFamiliarWith ?subject}";
        
        Reasoner reasoner = new GenericRuleReasoner(Rule.parseRules(rule));
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, inf);
        ResultSet resultSet = qe.execSelect();
        
        ResultSetFormatter.out(System.out, resultSet, query);
        qe.close();
    }
}
