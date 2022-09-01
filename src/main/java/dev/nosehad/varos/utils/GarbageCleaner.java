package dev.nosehad.varos.utils;

import dev.nosehad.varos.Main;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;

public class GarbageCleaner extends Thread {
        long millis;
        int nanos;
        boolean del = false;
        public GarbageCleaner () {
            millis = 1000;
            nanos = 0;
            start ();
        }

        public void delete() {
            this.del = true;
        }

        public void setSpeed (float val) {
            float t = 1000.0f / val;
            String s = String.valueOf ( t ) + "f";
            if(!s.contains ( "." )) {
                millis = ( long ) t;
            } else {
                millis = Long.parseLong ( StringUtils.substringBefore ( s, "." ) );
                nanos = Integer.parseInt ( StringUtils.substringBetween (  s,".", "f"  ));
            }
        }

        @Override
        public void run () {
            Bukkit.getScheduler ( ).runTask ( Main.getInstance ( ) , new Runnable ( ) {
                @Override
                public void run () {
                    System.gc ();
                }
            } );
            try {
                sleep ( millis, nanos );
                if (!this.del) {
                    dev.nosehad.varos.utils.reset.pull ( new Thread ( this ) );
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace ( );
            }
        }
    }
