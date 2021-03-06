package Ex1;

import java.text.DecimalFormat;

public class ComplexFunction implements complex_function
{

	private function func1;
	private function func2;
	private Operation op;
	private String opString;
	private static DecimalFormat df2 = new DecimalFormat("#.#######");
	/*
	 * Initialize only func1 and op is None.
	 */
	public ComplexFunction(function f1)
	{
		this.opString = "none";
		this.func1 = f1;
		this.op = Operation.None;
	}
	/*
	 * Initialize the functions and op is initialized by string. (Such as "mult","plus","div","max","min","comp").
	 */
	public ComplexFunction(String action, function f1,function f2) 
	{
		this.opString = action;
		this.func1 = f1;
		this.func2 = f2;

		switch(action) {
		case "plus": op = Operation.Plus;
		break;
		case "mult": op = Operation.Times;
		break;
		case "mul": op = Operation.Times;
		break;
		case  "div": op = Operation.Divid;
		break;
		case "min" :op = Operation.Min;
		break;
		case "max":op = Operation.Max;
		break;
		case "comp":op = Operation.Comp;
		break;
		case "none":op = Operation.None;
		break;
		default: 
		{op = Operation.Error;
		throw new  ArithmeticException("eror, operation do not exist");
		}
		}



	}

	/*
	 *  Initialize this Complex Function with given parameters.
	 */
	public ComplexFunction(Operation op,function f1, function f2)
	{

		this.opString = "none";
		this.func1 = f1;

		switch(op) {
		case Plus: opString = "plus";
		break;
		case Times: opString = "mult";
		break;
		case  Divid: opString = "div";
		break;
		case Min :opString = "min";
		break;
		case Max: opString= "max";
		break;
		case Comp:opString = "comp";
		break;
		case None:opString = "none";
		break;
		default: 
		{
			opString = "Eror";
			throw new  ArithmeticException("eror, operation do not exist");
		}
		}




	}
	/*
	 *  Computes the y value in x given.
	 */
	@Override
	public double f(double x)  {

		switch(op)
		{
		case Plus:
		{
			double rightSide = this.right().f(x);
			double leftSide = this.left().f(x);
			return rightSide+leftSide;
		}
		case Times:
		{
			double rightSide = this.right().f(x);
			double leftSide = this.left().f(x);
			return rightSide*leftSide;
		}
		case Divid:
		{
			double rightSide = this.right().f(x);
			double leftSide = this.left().f(x);
			return leftSide/rightSide;
		}
		case Min:
		{
			double leftSide = this.left().f(x);
			double rightSide = this.right().f(x);
			return Math.min(leftSide,rightSide);
		}

		case Max:
		{
			double leftSide = this.left().f(x);
			double rightSide = this.right().f(x);
			return Math.max(leftSide,rightSide);
		}
		case Comp:
		{
			double rightSide = this.right().f(x);
			double left = this.left().f(rightSide);
			return left;
		}
		case None:
		{
			return this.left().f(x);
		}

		default: throw new ArithmeticException("Operation do not exist");
		}

	}
	/*
	 * Returns function type object from a given string. correct string: action(func1,func2), action = {"mult","plus","div","max","min","comp"}, func1, func2 are functions like "2x^2","plus(2x,x)"...
	 */
	@Override
	public function initFromString(String s) {
		chekIfLegeal(s);
		function f1,f2;
		Operation o;
		s=s.replace(" ","");
		String oStr=new String("");
		int start = 0;

		try {

			if(s.length() <4) {
				return new Polynom(s);
			}
			if(s.charAt(3)=='(')
				start = 4;
			else
				start = 5;
			oStr = s.substring(0,start-1);
			
			int end_brackes = this.end2brackes(s.substring(start));
			o = this.getO(oStr);
			if(end_brackes==-1)
			{
				
				String onlyBitui = s.substring(start,s.length()-1);
				String[] funcs = onlyBitui.split(",");
				if(funcs.length==1)
				{
					return new ComplexFunction(new Polynom(funcs[0]));
				}
				else {
					return new ComplexFunction(oStr,new Polynom(funcs[0]),new Polynom(funcs[1]));
				}


			}
			else if(end_brackes!=0)
			{
				end_brackes +=start;
				f2 = initFromString(s.substring(end_brackes+2,s.length()-1));
				f1 = initFromString(s.substring(start,end_brackes+1));
				return new ComplexFunction(oStr,f1,f2);
			}
			else
			{

				return new Polynom(s);
			}


		}
		catch(Exception e)
		{
			//maybe its complex function or 
			try
			{
				String[] s2 = new String[2] ;
				int indexFirst = s.substring(start).indexOf(",");
				s2[0] = s.substring(start).substring(0,indexFirst);
				s2[1] = s.substring(start).substring(indexFirst+1,s.substring(start).length()-1);
				f2 = initFromString(s2[1]);
				f1 = initFromString(s2[0]);
				return new ComplexFunction(oStr,f1,f2);
			}
			catch(Exception e1)
			{
				System.out.println(e1);
				System.out.println("in correct string from file");
				return null;
			}

		}

	}
	/*
	 * return the index that end the first'(' for example "plus(2x,x)" return 9.
	 */
	private int end2brackes(String s) {
		int counter = 0;
		boolean seeFirst = false;

		for(int i =0; i<s.length();i++)
		{
			if(s.charAt(i)=='(')
			{
				counter++;
				seeFirst=true;
			}
			else if(s.charAt(i)==')')
				counter--;

			if(seeFirst&&counter==0)
				return i;
		}

		return counter;
	}


	private Operation getO(String oStr) {
		switch(oStr) {
		case "plus": return Operation.Plus;

		case "mult": return Operation.Times;
		
		case "mul": return Operation.Times;

		case  "div": return Operation.Divid;

		case "min" :return Operation.Min;

		case "max":return Operation.Max;

		case "comp":return Operation.Comp;

		case "none":return Operation.None;

		default: 
			return Operation.Error;}}




	/*
	 * Return function with a deep copy of this Complex Function.
	 */
	@Override
	public function copy() {
		ComplexFunction c = new ComplexFunction(new String(this.opString),func1.copy(),func2.copy());
		return c;
	/*
	 * Add to this ComplexFunction the func1 ComplexFunction
	 */
	}

	@Override
	public void plus(function f1) {
		if(func2 == null)
		{
			func2 = f1;
			this.op = Operation.Plus;
			this.opString = "plus";
		}
		else
		{
			function copy =this.copy();
			this.func1  =copy;
			this.func2 = f1;
			this.op = Operation.Plus;
			this.opString = "plus";
		}


	}
	/*
	 * Multiply this ComplexFunction with func1 ComplexFunction.
	 */
	@Override
	public void mul(function f1) {
		if(func2 == null)
		{
			func2 = f1;
			this.op = Operation.Times;
			this.opString = "mult";
		}
		else
		{
			function copy =this.copy();
			this.func1  =copy;
			this.func2 = f1;
			this.op = Operation.Times;
			this.opString = "mult";
		}

	}
	/*
	 * Divide this ComplexFunction with func1 ComplexFunction.
	 */
	@Override
	public void div(function f1) {
		if(func2 == null)
		{
			func2 = f1;
			this.op = Operation.Divid;
			this.opString = "div";
		}
		else
		{
			function copy =this.copy();
			this.func1  =copy;
			this.func2 = f1;
			this.op = Operation.Divid;
			this.opString = "div";
		}

	}
	/*
	 * Compute the Maximum between this ComplexFunction and func1 ComplexFunction.
	 */
	@Override
	public void max(function f1) {
		if(func2 == null)
		{
			func2 = func1.copy();
			func1 = f1;
			this.op = Operation.Max;
			this.opString = "max";
		}
		else
		{
			function copy =this.copy();
			this.func1  =f1;
			this.func2 = copy;
			this.op = Operation.Max;
			this.opString = "max";
		}

	}
	private static void chekIfLegeal(String s) {
		int[] legal = {0,0,0};
		//legal[0] = '('
		//legal[1] = ')'
		//legal[2] = ','
		char currentChar =0;
		for(int i=0;i<s.length();i++) {
			if(legal[1]>legal[0]) {
				throw new ArithmeticException("The order of parentheses is invalid");
			}else if(legal[0]<legal[2]) {
				throw new ArithmeticException("The order of ',' is invalid");
			}
			currentChar =s.charAt(i);

			if(currentChar=='(') {
				legal[0]++;
			}else if(currentChar==')'){
				legal[1]++;
			}else if(currentChar==',') {
				legal[2]++;
			}
		}
		if(legal[0]!=legal[1]&&legal[0]!=legal[2]) {
			throw new ArithmeticException("The order of parentheses or ',' is invalid");
		}
	}
	/*
	 * Compute the Minimum between this ComplexFunction and func1 ComplexFunction.
	 */
	@Override
	public void min(function f1) {
		if(func2 == null)
		{
			func2 = f1;
			this.op = Operation.Min;
			this.opString = "min";
		}
		else
		{
			function copy =this.copy();
			this.func1  =copy;
			this.func2 = f1;
			this.op = Operation.Min;
			this.opString = "min";
		}

	}
	/*
	 *  Wraps the func1 ComplexFunction with this ComplexFunction: this.f(func1(x)).
	 */
	@Override
	public void comp(function f1) {
		if(func2 == null)
		{
			func2 = f1;
			this.op = Operation.Comp;
			this.opString = "comp";
		}
		else
		{
			function copy =this.copy();
			this.func1  =copy;
			this.func2 = f1;
			this.op = Operation.Comp;
			this.opString = "comp";
		}
	}
	/*
	 *  Returns the left function. (func1)
	 */
	@Override
	public function left() {
		return func1;
	}
	/*
	 *  Returns the right function. (func2). 
	 */
	@Override
	public function right() {
		return func2;
	}
	/*
	 * Returns the operation.
	 */
	@Override
	public Operation getOp() {
		return this.op;
	}
	/*
	 * Print the function as: "op(func1,func2)".
Static functions, Getters and Setters:
	 */
	@Override
	public String toString() {

		String s =new String();
		if(func2!= null)
			s+= this.opString+"("+func1.toString()+","+func2.toString()+")";
		else
			s+= this.opString+"("+func1.toString()+")";
		return s;

	}
	/*
	 * Returns true if and only if this ComplexFunction is logically equals to obj in the range [-10000,10000]
	 */
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof function)
		{
			function comp = (function)o;
			Range rangeCheck = new Range(-10000,10000);
			for(double i=  (int)rangeCheck.get_min();i<rangeCheck.get_max();i+=0.30)
			{
				if(Math.abs(foo(this.f(i))-foo(comp.f(i)))>Monom.EPSILON)
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	/*
	 *  return f(x) with only 9 numbers after the point.
	 *	Functions_GUI
	 */
	private double foo(double x)
	{
		int y= (int) (x/100000000);
		return y;
	}



}
