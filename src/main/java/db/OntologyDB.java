package db;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

/**
 *
 * @author richou
 */
public class OntologyDB {
    /**
     * Creates a new connection to a database.
     * 
     * @param DB_URL
     * @param DB_USER
     * @param DB_PASSWD
     * @param DB_NAME
     * 
     * @return 
     */
    public static IDBConnection connectDB(String DB_URL, String DB_USER, String DB_PASSWD, String DB_NAME) {
    	return new DBConnection(DB_URL, DB_USER, DB_PASSWD, DB_NAME);
    } 

    /**
     * Read ontology from a file and store it into the connected database.
     * 
     * @param connection    A connection to the database
     * @param name  Name of the new model
     * @param fileName  Ontology file name
     * 
     * @return 
     */
    public static OntModel createDBModelFromOntology(IDBConnection connection, String name, String fileName) {
    	ModelMaker maker = ModelFactory.createModelRDBMaker(connection);
    	Model base = maker.createModel(name);
    	OntModel newModel = ModelFactory.createOntologyModel(getModelSpec(maker), base);
        
   	newModel.read(fileName); // This is the important line.
        
    	return newModel;
    }

    /**
     * Fetches a ontology model from the connected database.
     * 
     * @param connection    A connection to the database
     * @param name  The name of the ontology model to fetch
     * 
     * @return 
     */
    public static OntModel getModelFromDB(IDBConnection connection, String name) {
    	ModelMaker maker = ModelFactory.createModelRDBMaker(connection);
    	Model base = maker.getModel(name);
    	OntModel newModel = 	ModelFactory.createOntologyModel( getModelSpec(maker), base);
        
    	return newModel;
    }

    /**
     * Fetches the particuliar specification of the maker.
     * 
     * @param maker
     * 
     * @return 
     */
    public static OntModelSpec getModelSpec(ModelMaker maker) {
            OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
            spec.setImportModelMaker(maker);
            return spec;
    }
}
