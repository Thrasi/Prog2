

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/*
 * This allows me to control the focus in my JOptionPanes.  There may be
 * a more appropriate way to do this but I have not found it.  I found this
 * on stack overflow. 
 */
class RequestFocusClass implements AncestorListener {
	@Override
	public void ancestorAdded(AncestorEvent ae) {
		ae.getComponent().requestFocusInWindow();
	}
	
	@Override
	public void ancestorMoved(AncestorEvent arg0) {}
	@Override
	public void ancestorRemoved(AncestorEvent arg0) {}
}
