package cn.sowjz.souwen.v1.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.sowjz.souwen.v1.SouwenClient;
import cn.sowjz.souwen.v1.conf.SouwenConfig;
import cn.sowjz.souwen.v1.doc.Doc;
import cn.sowjz.souwen.v1.util.FileUtil;





public class FeedExportDat {

	public void feed(SouwenClient ss ,String file) throws Exception {
		
		
		
		
		
	    List<Doc> as=new ArrayList<Doc>(1000);
		
			
			
			
			SSDataReader dr=new SSDataReader(ss);
			
			dr.open(file);
			while(true)
			{  Doc a=dr.next();
			   if(a==null)
				   break;
			   as.add(a);
			   if(as.size()==1000)
			   {
				   ss.addDoc(as);
				   as.clear();
			   }   
		    	
			}
			dr.close();
			
			 if(as.size()>0)
			   {
				   ss.addDoc(as);
				   as.clear();
			   }   
			
			ss.commit();
			
	}
	
	public static void main(String []argv)throws Exception
	{
	
		String file=null;
	
		System.out.println("FeedExportDat [export dat file]");
		if(argv.length>0)
			file=argv[0];	
		if(file==null)
		{
			System.out.println("error : undefine [export dat file]!");
			return;
		}	
		if(new File(file).exists()==false)
		{
			System.out.println("error : can not found file "+file);
			return;
		}	
		
		System.out.println(" data_file="+file);
		Thread.sleep(2000);
	
		
		SouwenClient ss =new  SouwenClient( new SouwenConfig(FileUtil.loadPropertiesFromFile("conf/SearchSystem.conf", "UTF8")));
		
		FeedExportDat aad=new FeedExportDat();
		
		aad.feed(ss,file);
		System.out.println("feed data finished.");
		ss.destroy();
	}

	
}
