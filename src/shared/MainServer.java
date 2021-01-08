package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MainServer extends Remote {
  String toUpperCase(String argument) throws RemoteException;
  int registerNode(NodeServer node) throws RemoteException;
  Integer[] nodes() throws RemoteException;
  NodeServer getNode(int id) throws RemoteException;
}
