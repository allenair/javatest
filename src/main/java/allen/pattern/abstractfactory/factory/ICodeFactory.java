package allen.pattern.abstractfactory.factory;

import allen.pattern.abstractfactory.product.IDotnetCode;
import allen.pattern.abstractfactory.product.IJavaCode;


public interface ICodeFactory {
	public IDotnetCode createDotnetCode();
	public IJavaCode createJavaCode();
}
