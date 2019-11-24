/*
 * Copyright (c) 2019, Wolfgang Reder
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *  without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package eu.hansolo.steelseries.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.Timer;

/**
 *
 * @author Wolfgang Reder
 */
public final class BlinkTimer
{

  private final Timer timer;
  private final Set<ActionListener> listener = new CopyOnWriteArraySet<>();
  private boolean on;

  public BlinkTimer()
  {
    this.timer = new Timer(500,
                           null);
    this.timer.setRepeats(true);
    this.timer.addActionListener(this::onAction);

  }

  public void start()
  {
    timer.start();
  }

  public void stop()
  {
    timer.stop();
  }

  public void stop(boolean state)
  {
    timer.stop();
    on = state;
    fireActionEvent(null);
  }

  public boolean isOn()
  {
    return on;
  }

  public void setOn(boolean o)
  {
    if (on != o) {
      on = o;
      fireActionEvent(null);
    }
  }

  public boolean isRunning()
  {
    return timer.isRunning();
  }

  public void setRunning(boolean r)
  {
    if (r) {
      start();
    } else {
      stop();
    }
  }

  public void setDelay(int delay)
  {
    timer.setDelay(delay);
  }

  public int getDelay()
  {
    return timer.getDelay();
  }

  private void onAction(ActionEvent e)
  {
    on = !on;
    fireActionEvent(e);
  }

  private void fireActionEvent(ActionEvent e)
  {
    if (!listener.isEmpty()) {
      long when = e != null ? e.getWhen() : Instant.now().toEpochMilli();
      int mod = e != null ? e.getModifiers() : 0;
      ActionEvent evt = new ActionEvent(this,
                                        ActionEvent.ACTION_PERFORMED,
                                        timer.getActionCommand(),
                                        when,
                                        mod);
      for (ActionListener l : listener) {
        l.actionPerformed(evt);
      }
    }
  }

  public void addActionListener(ActionListener l)
  {
    if (l != null) {
      listener.add(l);
    }
  }

  public void removeActionListener(ActionListener l)
  {
    listener.remove(l);
  }

}
