package inter;

/**
 * 四元式
 * @author Hu Yuxi
 *
 */
public class FourElement {
	private int id;// 四元式序号，为编程方便
	private String op;// 操作符
	private String arg1;// 第一个操作数
	private String arg2;// 第二个操作数
	private Object result;// 结果

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
		this.setId(id);
		this.setOp(op);
		this.setArg1(arg1);
		this.setArg2(arg2);
		this.setResult(result);
	}

	/*
	 * getResult
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/*
	 * getAtg1
	 */
	public String getArg1() {
		return arg1;
	}

	/*
	 * setArg1
	 */
	public void setArg1(String arg1) {
		this.arg1 = arg1;
	}

	/*
	 * getArg2
	 */
	public String getArg2() {
		return arg2;
	}

	/*
	 * setArg2
	 */
	public void setArg2(String arg2) {
		this.arg2 = arg2;
	}

	/*
	 * getId
	 */
	public int getId() {
		return id;
	}

	/*
	 * setId
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * getOp
	 */
	public String getOp() {
		return op;
	}

	/**
	 * setOp
	 * @param op
	 */
	public void setOp(String op) {
		this.op = op;
	}
}
