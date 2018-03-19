function foo(s){
//	return s+1;
	return mapjs.get("third");
}

function bar(s){
//	return s*2;
//	return mapjs.get("beaninner").getSecondStr();
	return mapjs.beaninner.secondStr;
}

function showbean(s){
	if(s=='1'){
		return beanjs.firstStr;
	}else{
		return beanjs.getSecondStr();
	}
}
