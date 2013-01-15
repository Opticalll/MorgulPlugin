package cz.opt.morgulplugin.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.opt.morgulplugin.MorgulPlugin;
import cz.opt.morgulplugin.runnable.SocketHandler;

public class ThreadServer implements Runnable
{
	private ServerSocket socket;
	private int port = 7778;
	private ExecutorService threadPool;
	private boolean running = true;
	
	public ThreadServer(int maxThreadCount)
	{
		threadPool = Executors.newFixedThreadPool(maxThreadCount);
	}
	
	@Override
	public void run()
	{
		try
		{
		openServerSocket();
		} catch(RuntimeException ex) {
			MorgulPlugin.log(ex.getMessage());
			return;
		}
		while(isRunning())
		{
			Socket clientSocket = null;
			try
			{
				clientSocket = socket.accept();
				MorgulPlugin.log("Socket Accepted. " + clientSocket.getRemoteSocketAddress());
			} catch (IOException e) {
				if(!isRunning()) {
                    MorgulPlugin.log("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
			}
			 this.threadPool.execute( new SocketHandler(clientSocket));

		}
		this.threadPool.shutdown();
        MorgulPlugin.log("Server Stopped.") ;
	}
	
	private synchronized boolean isRunning() {
        return this.running;
    }

    public synchronized void stop(){
        this.running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

	
	private void openServerSocket() {
        try 
        {
            this.socket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }


}
