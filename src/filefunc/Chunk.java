package filefunc;

import message.Message;

public class Chunk
{
    private String fileId;
    private int chunkNr;
    private int replicationDegree;

    Chunk(String fileId, int chunkNr, int replicationDeg){
        this.fileId = fileId;
        this.chunkNr = chunkNr;
        this.replicationDegree = replicationDeg;
    }

    Chunk(Message m){
        this.fileId = m.head.getFileId();
        this.chunkNr = m.head.getChunkNr();
        this.replicationDegree = m.head.getReplicationDeg();
    }

    public String getFileId(){
        return fileId;
    }

    public int getChunkNr(){
        return chunkNr;
    }

    public int getReplicationDeg(){
    	return replicationDegree; 
    	}

    @Override
    public String toString(){
        return "Chunk{" +
                ", fileId='" + fileId + '\'' +
                ", chunkNr=" + chunkNr + '\'' +
                ", replicationDegree=" + replicationDegree +
                '}';
    }

    public String simple(){
        return  fileId  + " , " +
                chunkNr + " , " +
                replicationDegree ;
    }
}
