package qub;

public class SwingUIBase extends AWTUIBase
{
    protected SwingUIBase(Display display, AsyncRunner asyncRunner)
    {
        super(display, asyncRunner);
    }

    public static SwingUIBase create(Display display, AsyncRunner asyncRunner)
    {
        return new SwingUIBase(display, asyncRunner);
    }

    public static SwingUIBase create(Process process)
    {
        return SwingUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner());
    }

    /**
     * Register the provided callback to be invoked when the provided component's text changes.
     * @param jTextComponent The component to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided component.
     */
    public Disposable onTextChanged(javax.swing.text.JTextComponent jTextComponent, Action1<String> callback)
    {
        PreCondition.assertNotNull(jTextComponent, "jTextComponent");
        PreCondition.assertNotNull(callback, "callback");

        final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener()
        {

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e)
            {
                callback.run(jTextComponent.getText());
            }
        };
        jTextComponent.getDocument().addDocumentListener(documentListener);
        return Disposable.create(() -> jTextComponent.getDocument().removeDocumentListener(documentListener));
    }
}
