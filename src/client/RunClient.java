package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class RunClient {
  public static void main(String[] args) throws RemoteException, NotBoundException {
    ClientImplementation client = new ClientImplementation();

    Scanner in  = new Scanner(System.in);

    while(true) {
      System.out.println("Command > ");
      String command = in.nextLine();
      int nodeId = 0;
      String text = "";

      switch (command.toLowerCase()) {
        case "help":
          System.out.println("Available commands are: nodes, list, get, exit");
          break;
        case "nodes":
          Integer[] nodes = client.nodes();
          System.out.println("Available nodes ids: ");
          for (Integer id : nodes) {
            System.out.println("    "+id);
          }
          break;
        case "list":
          System.out.println("NodeID: ");
          nodeId = Integer.parseInt(in.nextLine());
          List<String> list = client.listFiles(nodeId);
          System.out.println("Available files: ");
          for (String s : list) {
            System.out.println("    "+s);
          }
          break;
        case "get":
          System.out.println("NodeID: ");
          nodeId = Integer.parseInt(in.nextLine());
          System.out.println("Remote Filepath: ");
          text = in.nextLine();
          boolean found = client.get(nodeId,text);
          if(!found) {
            System.out.println("File not found.");
          }
          break;
        case "send":
          System.out.println("NodeID: ");
          nodeId = Integer.parseInt(in.nextLine());
          System.out.println("Filepath: ");
          text = in.nextLine();
          System.out.println("Uploading file ...");
          boolean saved = client.send(text, nodeId);
          if(!saved) {
            System.out.println("File couldn't be saved.");
          } else {
            System.out.println("File saved successfully.");
          }
          break;
        case "uppercase":
          System.out.println("Input > ");
          text = in.nextLine();
          String result = client.toUpperCase(text);
          System.out.println("Response > "+result);
          break;
        case "exit":
          System.out.println("Have a nice day!");
          break;
        default:
          System.out.println("Bad command");
      }
    }
  }
}
