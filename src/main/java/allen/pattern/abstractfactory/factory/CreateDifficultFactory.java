package allen.pattern.abstractfactory.factory;

import allen.pattern.abstractfactory.product.DifficultDotnetCode;
import allen.pattern.abstractfactory.product.DifficultJavaCode;
import allen.pattern.abstractfactory.product.IDotnetCode;
import allen.pattern.abstractfactory.product.IJavaCode;

public class CreateDifficultFactory implements ICodeFactory {

	@Override
	public IDotnetCode createDotnetCode() {
		return new DifficultDotnetCode();
	}

	@Override
	public IJavaCode createJavaCode() {
		return new DifficultJavaCode();
	}

}
