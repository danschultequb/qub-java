package qub;

import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Window extends DisposableBase
{
    private final AsyncRunner mainAsyncRunner;
    private final BasicAsyncAction windowClosedTask;
    private final JFrame jFrame;
    private boolean disposed;

    public Window(AsyncRunner mainAsyncRunner)
    {
        this.mainAsyncRunner = mainAsyncRunner;
        this.windowClosedTask = new BasicAsyncAction(mainAsyncRunner);

        this.jFrame = new JFrame();
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent e)
            {
            }

            @Override
            public void windowClosing(WindowEvent e)
            {
            }

            @Override
            public void windowClosed(WindowEvent e)
            {
                dispose();
            }

            @Override
            public void windowIconified(WindowEvent e)
            {
            }

            @Override
            public void windowDeiconified(WindowEvent e)
            {
            }

            @Override
            public void windowActivated(WindowEvent e)
            {
            }

            @Override
            public void windowDeactivated(WindowEvent e)
            {
            }
        });
    }

    /**
     * Wait for this Window to close. This will block the current thread.
     */
    public void awaitClose()
    {
        windowClosedTask.await();
    }

    /**
     * Get whether or not this Window is visible.
     * @return Whether or not this Window is visible.
     */
    public boolean isVisible()
    {
        return jFrame.isVisible();
    }

    /**
     * Set whether or not this Window is visible.
     * @param visible Whether or not this Window is visible.
     */
    public void setVisible(boolean visible)
    {
        jFrame.setVisible(visible);
    }

    /**
     * Get the title of this Window.
     * @return The title of this Window.
     */
    public String getTitle()
    {
        return jFrame.getTitle();
    }

    /**
     * Set the title of this Window.
     * @param title The title of this Window.
     */
    public void setTitle(String title)
    {
        PreCondition.assertNotNull(title, "title");

        jFrame.setTitle(title);

        PostCondition.assertEqual(title, getTitle(), "getTitle()");
    }

    @Override
    public boolean isDisposed()
    {
        return disposed;
    }

    @Override
    public Result<Boolean> dispose()
    {
        Result<Boolean> result;

        if (disposed)
        {
            result = Result.successFalse();
        }
        else
        {
            disposed = true;

            jFrame.dispose();

            windowClosedTask.schedule();
            awaitClose();

            result = Result.successTrue();
        }

        PostCondition.assertNotNull(result, "result");

        return result;
    }
}