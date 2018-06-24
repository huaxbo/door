package com.am.licence.xml;


import org.jdom.*;
import org.jdom.input.*;
import java.io.FileInputStream;



public class ParseLicenceXmlFile {
  public ParseLicenceXmlFile() {
  }


  /**
   *
   * @return
   */
  public Document createDom(FileInputStream in) {
    Document doc = null;
    try {
        SAXBuilder sb = new SAXBuilder();
        doc = sb.build(in);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return doc;
  }

  /**
   *
   * @param doc Document
   * @return String[]
   */
  public String getDomainNum(Document doc){

    if (doc == null) {
      return null;
    }

    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }

    Element domains = root.getChild("domains");
    if (domains == null) {
      return null;
    }
    String r = domains.getText() ;

    if(r == null || r.trim().equals("")){
      return null ;
    }


    return r;

  }

  /**
   *
   * @param doc Document
   * @return String[]
   */
  public String getEValue(Document doc){

    if (doc == null) {
      return null;
    }

    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }

    Element publicKey = root.getChild("publicKey");
    if (publicKey == null) {
      return null;
    }

    Element eValue = publicKey.getChild("eValue") ;
    if (eValue == null) {
      return null;
    }

    String r = eValue.getText() ;

    if(r == null || r.trim().equals("")){
      return null ;
    }


    return r;

  }
  /**
   *
   * @param doc Document
   * @return String[]
   */
  public String getNValue(Document doc){

    if (doc == null) {
      return null;
    }

    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }

    Element publicKey = root.getChild("publicKey");
    if (publicKey == null) {
      return null;
    }

    Element nValue = publicKey.getChild("nValue") ;
    if (nValue == null) {
      return null;
    }

    String r = nValue.getText() ;

    if(r == null || r.trim().equals("")){
      return null ;
    }

    return r;

  }
  /**
   *
   * @param doc Document
   * @return String
   */
  public String getId(Document doc){
    if (doc == null) {
      return null;
    }

    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }

    Element id = root.getChild("id");
    if (id == null) {
      return null;
    }

    String r = id.getText() ;

    if(r == null || r.trim().equals("")){
      return null ;
    }

    return r ;

  }

  /**
   *
   * @param doc Document
   * @return String
   */
  public String getSignedValue(Document doc){
    if (doc == null) {
      return null;
    }

    Element root = doc.getRootElement();
    if (root == null) {
      return null;
    }

    Element signedValue = root.getChild("signedValue");
    if (signedValue == null) {
      return null;
    }

    String r = signedValue.getText() ;

    if(r == null || r.trim().equals("")){
      return null ;
    }

    return r ;

  }


}
