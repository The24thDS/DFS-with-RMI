package node;

import shared.Client;
import shared.MainServer;
import shared.NodeServer;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class NodeImplementation implements NodeServer {
  private List<String> files;
  private int id;
  private String baseDirectory;

  public NodeImplementation(String path) throws RemoteException, NotBoundException {
    UnicastRemoteObject.exportObject(this, 0);
    files = new ArrayList<>();
    baseDirectory = path;

    indexFiles(baseDirectory);

    Registry registry = LocateRegistry.getRegistry("localhost", 1099);
    MainServer server = (MainServer) registry.lookup("Server");
    id = server.registerNode(this);
    System.out.println("Connected to the main server");
    System.out.println("Received id #"+id);
  }

  @Override
  public List<String> listFiles() {
    return files;
  }

  @Override
  public boolean get(String filename, Client client) {
    File f = new File(baseDirectory+"/"+filename);
    try {
      FileInputStream fin = new FileInputStream(f);
      byte[] data = new byte[1024*1024];
      int len = fin.read(data);
      while(len>0) {
        client.createFile(f.getName(), data, len);
        len = fin.read(data);
      }
      fin.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public boolean createFile(String filename, byte[] data, int len) throws RemoteException {
    File f=new File(baseDirectory+"/"+filename);
    try {
      f.createNewFile();
      FileOutputStream out=new FileOutputStream(f,true);
      out.write(data,0,len);
      out.flush();
      out.close();
      System.out.println("Done writing data for" + filename);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  private void indexFiles(String directory) {
    System.out.println("Indexing files in "+directory+" ...");
    File f = new File(directory);
    File[] files = f.listFiles();
    for (File file : files) {
      if(!file.isDirectory()) {
        this.files.add(file.getPath().replace(baseDirectory+"/", "").replace(baseDirectory+"\\", ""));
      } else {
        indexFiles(file.getPath());
      }
    }
    System.out.println("Done indexing in "+directory+".");
  }
}
