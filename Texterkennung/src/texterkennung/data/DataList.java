package texterkennung.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import GUI.IGUI;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class DataList<E extends Data> extends Data implements List<E>
{
	private final ArrayList<E> data;
	
	public DataList(String name, boolean b)
	{
		super(name, b);
		this.data = new ArrayList<E>();
	}

	public boolean add(E data)
	{
		return this.data.add(data);
	}
	
	public E get(int index)
	{
		return this.data.get(index);
	}
	
	public ArrayList<E> get()
	{
		return this.data;
	}
	
	public int size()
	{
		return this.data.size();
	}

	@Override
	public void gui(BorderPane pane)
	{
		TabPane tabPane = new TabPane();
		for (IGUI iGUI : this.data)
		{
			Tab tab = new Tab();
			tab.setText(iGUI.getName());
			
			BorderPane pane2 = new BorderPane();
			
			iGUI.gui(pane2);
			
			tab.setContent(pane2);
			tabPane.getTabs().add(tab);
		}
		
		pane.setCenter(tabPane);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return this.data.addAll(c);
	}

	@Override
	public void clear() {
		this.data.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.data.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.data.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return this.data.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.data.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return this.data.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.data.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.data.retainAll(c);
	}

	@Override
	public Object[] toArray() {
		return this.data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.data.toArray(a);
	}

	@Override
	public void add(int index, E element) {
		this.data.add(index, element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.data.addAll(index, c);
	}

	@Override
	public int indexOf(Object o) {
		return this.data.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.data.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.data.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.data.listIterator(index);
	}

	@Override
	public E remove(int index) {
		return this.data.remove(index);
	}

	@Override
	public E set(int index, E element) {
		return this.data.set(index, element);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.data.subList(fromIndex, toIndex);
	}
}
