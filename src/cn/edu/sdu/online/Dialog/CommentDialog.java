package cn.edu.sdu.online.Dialog;

import cn.edu.sdu.online.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class CommentDialog extends Dialog{
	
	int layoutRes;//布局文件
    Context context;
    
    public CommentDialog(Context context) {
        super(context);
        this.context = context;
    }

	
	
	public CommentDialog(Context context,int resLayout){
        super(context, resLayout);
        this.context = context;
        this.layoutRes=resLayout;
    }
	
	public CommentDialog(Context context, int commentdialog,
			int commentdialog2) {
		super(context,commentdialog);
		// TODO Auto-generated constructor stub
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.commentdialog);
	}
}
