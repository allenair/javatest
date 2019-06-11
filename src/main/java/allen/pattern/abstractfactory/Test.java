package allen.pattern.abstractfactory;

import allen.pattern.abstractfactory.factory.CreateDifficultFactory;
import allen.pattern.abstractfactory.factory.CreateSimpleFactory;
import allen.pattern.abstractfactory.factory.ICodeFactory;
import allen.pattern.abstractfactory.product.IDotnetCode;
import allen.pattern.abstractfactory.product.IJavaCode;

public class Test {

	public static void main(String[] args) {
		Test tt = new Test();
		tt.createByDescription("java", "difficult");
	}
	
	public void createByDescription(String lang, String type){
		ICodeFactory factory;
		IJavaCode java=null;;
		IDotnetCode dotnet=null;
		
		if("simple".equalsIgnoreCase(type)){
			factory = new CreateSimpleFactory();
		}else{
			factory = new CreateDifficultFactory();
		}
		
		if("java".equalsIgnoreCase(lang)){
			java = factory.createJavaCode();
		}else{
			dotnet = factory.createDotnetCode();
		}
		
		
		if(java!=null)
			java.printJavaCodeComment();
		
		if(dotnet!=null)
			dotnet.printDotnetCodeComment();
	}
}
