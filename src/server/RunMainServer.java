package server;

import shared.MainServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunMainServer {

  public static void main(String[] args) throws RemoteException, AlreadyBoundException {
    MainServer server = new MainServerImplementation();

    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("Server", server);
    System.out.println("Server started");
  }
}
