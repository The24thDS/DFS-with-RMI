package server;

import shared.MainServer;
import shared.NodeServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class MainServerImplementation implements MainServer {
  private final HashMap<Integer, NodeServer> nodes;
  private final List<String> files;

  public MainServerImplementation() throws RemoteException {
    UnicastRemoteObject.exportObject(this, 0);
    nodes = new HashMap<>();
    files = new ArrayList<>();
  }

  @Override
  public String toUpperCase(String argument) {
    return argument.toUpperCase();
  }

  @Override
  public int registerNode(NodeServer node) throws RemoteException {
    Random r = new Random();
    int id = r.nextInt(100);

    if(id < 0) {
      id *= -1;
    }

    while(nodes.containsKey(id)) {
      r.nextInt();
    }

    nodes.put(id, node);
    files.addAll(node.listFiles());
    System.out.println("Node #"+id+" registered and file names added");
    return id;
  }

  @Override
  public Integer[] nodes() {
    return nodes.keySet().toArray(new Integer[nodes.size()]);
  }

  @Override
  public NodeServer getNode(int id) {
    return nodes.get(id);
  }
}