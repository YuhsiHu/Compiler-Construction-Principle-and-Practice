package inter;

public class FourElement {
	int id;// 四元式序号，为编程方便
	String op;// 操作符
	String arg1;// 第一个操作数
	String arg2;// 第二个操作数
	Object result;// 结果

	/**
	 * 无参构造函数
	 */
	public FourElement() {

	}

	/**
	 * 四元式有参构造函数
	 * @param id 四元式序号
	 * @param op 操作符
	 * @param arg1 操作数1
	 * @param arg2 操作数2
	 * @param result 结果
	 */
	public FourElement(int id, String op, String arg1, String arg2, String result) {
		this.id = id;
		this.op = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = result;
	}
}
