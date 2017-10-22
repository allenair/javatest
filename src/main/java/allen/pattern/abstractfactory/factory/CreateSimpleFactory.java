package allen.pattern.abstractfactory.factory;

import allen.pattern.abstractfactory.product.IDotnetCode;
import allen.pattern.abstractfactory.product.IJavaCode;
import allen.pattern.abstractfactory.product.SimpleDotnetCode;
import allen.pattern.abstractfactory.product.SimpleJavaCode;

public class CreateSimpleFactory implements ICodeFactory {

	@Override
	public IDotnetCode createDotnetCode() {
		return new SimpleDotnetCode();
	}

	@Override
	public IJavaCode createJavaCode() {
		return new SimpleJavaCode();
	}

}
