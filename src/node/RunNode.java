package node;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunNode {
  public static void main(String[] args) throws RemoteException, NotBoundException {
    String path = args[0];
    NodeImplementation node = new NodeImplementation(path);
  }
}
