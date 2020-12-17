package Protocol;

import java.io.File;


//Defines an interface for a protocol, that will be used to interface with the engine.
//Currently this interface is being based off of UCI, however In the future, stuff may be added or removed

public interface StatelessProtocol {
	public Object getEngineSignature();
	public boolean initProtocol(File EngineLoc);
	public boolean loadState(String FENState);
	public boolean changeState();
	public void sendStateChange();
	public void pauseEngine();
	public void startEngine();
	public void quit();
}
