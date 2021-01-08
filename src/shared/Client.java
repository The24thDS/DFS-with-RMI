package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
  boolean createFile(String filename, byte[] data, int len) throws RemoteException;
  boolean send(String filename, Integer nodeId) throws RemoteException;
}
