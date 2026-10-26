package marscolony.emergencies;

public abstract class Emergency{
private String location;
//private Responder agent; ~~abstract class Responder parent of personnel ans robots 
public Emergency(String location)
{
    this.location=location;
    //this.agent=agent;
}




}