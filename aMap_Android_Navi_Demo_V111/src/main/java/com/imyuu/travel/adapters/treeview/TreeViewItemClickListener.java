package com.imyuu.travel.adapters.treeview;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
/**
 * TreeView item����¼�
 * @author carrey
 *
 */
public class TreeViewItemClickListener implements OnItemClickListener {
	/** adapter */
	private TreeViewAdapter treeViewAdapter;
	private Context mContext;
	public TreeViewItemClickListener(TreeViewAdapter treeViewAdapter,Context ctx) {
		this.treeViewAdapter = treeViewAdapter;
		mContext =ctx;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//�����item����Ԫ��
		Element element = (Element) treeViewAdapter.getItem(position);
		//���е�Ԫ��
		ArrayList<Element> elements = treeViewAdapter.getElements();
		//Ԫ�ص����Դ
		ArrayList<Element> elementsData = treeViewAdapter.getElementsData();
		
		//���û�������itemֱ�ӷ���
		if (!element.isHasChildren()) {
			Toast.makeText(mContext, "û�������item", Toast.LENGTH_SHORT);
			return;
		}
		
		if (element.isExpanded()) {
			element.setExpanded(false);
			//ɾ��ڵ��ڲ���Ӧ�ӽڵ���ݣ������ӽڵ���ӽڵ�...
			ArrayList<Element> elementsToDel = new ArrayList<Element>();
			for (int i = position + 1; i < elements.size(); i++) {
				if (element.getLevel() >= elements.get(i).getLevel())
					break;
				elementsToDel.add(elements.get(i));
			}
			elements.removeAll(elementsToDel);
			treeViewAdapter.notifyDataSetChanged();
		} else {
			element.setExpanded(true);
			//�����Դ����ȡ�ӽڵ������ӽ�����ע������ֻ���������һ���ӽڵ㣬Ϊ�˼��߼�
			int i = 1;//ע������ļ���������for������ܱ�֤������Ч
			for (Element e : elementsData) {
				if (e.getParendId() == element.getId()) {
					e.setExpanded(false);
					elements.add(position + i, e);
					i ++;
				}
			}
			treeViewAdapter.notifyDataSetChanged();
		}
	}

}
