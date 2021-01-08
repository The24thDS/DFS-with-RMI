package client;

import shared.Client;
import shared.MainServer;
import shared.NodeServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ClientImplementation implements Client {

  private final MainServer server;

  public ClientImplementation() throws RemoteException, NotBoundException {
    UnicastRemoteObject.exportObject(this, 0);
    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    server = (MainServer) registry.lookup("Server");
  }

  public String toUpperCase(String argument) {
    String s = null;
    try {
      s = server.toUpperCase(argument);
    } catch (RemoteException e) {
      System.out.println("Error: Couldn't connect to the server");
    }
    return s;
  }

  public Integer[] nodes() {
    Integer[] response = null;
    try {
      response = server.nodes();
    } catch (RemoteException e) {
      System.out.println("Error: Couldn't connect to the server");
    }
    return response;
  }

  public List<String> listFiles(int nodeId) {
    List<String> l = new ArrayList<>();
    try {
      l = server.getNode(nodeId).listFiles();
    } catch (RemoteException e) {
      e.printStackTrace();
      System.out.println("Error: Couldn't connect to the server");
    }
    return l;
  }

  @Override
  public boolean createFile(String filename, byte[] data, int len) {
    File f=new File(filename);
    try {
      f.createNewFile();
      FileOutputStream out=new FileOutputStream(f,true);
      out.write(data,0,len);
      out.flush();
      out.close();
      System.out.println("Done writing data for "+filename);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean send(String filename, Integer nodeId) throws RemoteException {
    NodeServer node = server.getNode(nodeId);
    File f = new File(filename);
    try {
      FileInputStream fin = new FileInputStream(f);
      byte[] data = new byte[1024*1024];
      int len = fin.read(data);
      while(len>0) {
        node.createFile(f.getName(), data, len);
        len = fin.read(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean get(int nodeId, String filename) {
    System.out.println("Trying to get "+filename+" from node #"+nodeId);
    try {
      NodeServer node = server.getNode(nodeId);
      return node.get(filename, this);
    } catch (RemoteException e) {
      System.out.println("Error: Couldn't contact the node.");
    }
    return false;
  }
}
