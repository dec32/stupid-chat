package view;

import java.io.OutputStream;
import java.io.PrintStream;

import javafx.scene.control.TextArea;

public class TextAreaPrintStream extends PrintStream{
	
	private TextArea ta;
	
	public TextAreaPrintStream(OutputStream ops,TextArea ta) {
		/*
		 * System.out 是一个 PrintStream, 其各种 print 函数最终都会调用 write 方法
		 * 在PrintStream的构造方法里,一定要提供一个 OutputStream,
		 * 因为PrintStream 的 write 方法会把字节写到这个 OutputStream里
		 * 但是我们不希望这些字节跑到别的地方, 所以我们重写 write 方法, 把这些字节转成字符串, 然后追加到 TextArea里面
		 * 但是这不意味着我们可以不提供 OutputStream, 因为 PrintStream 并没有 PrintStream() 这个构造方法
		 * 所以我们还是需要在外部提供一个没有任何意义的 OutputStream, 然后调用 super(ops), 完成初始化
		 */
		super(ops);
		this.ta = ta;
	}
	
	
	/*
	 * 重写write 方法, 把字节数组 buf 转成字符串, 追加到TextArea里面
	 */
    @Override
    public void write(byte[] buf, int off, int len){
        String str = new String(buf, off, len); 
        ta.appendText(str);
    }
}
