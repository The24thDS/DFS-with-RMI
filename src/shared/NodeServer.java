package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NodeServer extends Remote {
  List<String> listFiles() throws RemoteException;
  boolean get(String filename, Client client) throws RemoteException;
  boolean createFile(String filename, byte[] data, int len) throws RemoteException;
}
