/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diskuto.helpers;

import org.exist.xmldb.EXistXQueryService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

/**
 *
 * @author dario
 */
public class Database {
    
    private final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private final String driver = "org.exist.xmldb.DatabaseImpl";
    private final String collection = "/db/diskuto";
    private final org.xmldb.api.base.Database database;
    private final Collection col;

    public Database() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
        Class c = Class.forName(driver);
        database = (org.xmldb.api.base.Database) c.newInstance();
        DatabaseManager.registerDatabase(database);
        col = DatabaseManager.getCollection(URI+collection, "admin", "");
    }
    
    public void close() throws XMLDBException{
        col.close();
        DatabaseManager.deregisterDatabase(database);
    }
    
    public ResourceSet xquery(String upit) throws XMLDBException{
        EXistXQueryService service = (EXistXQueryService) col.getService("XQueryService", "1.0");
        service.setProperty("indent", "yes");
        return service.query(upit);
    }
    
}
