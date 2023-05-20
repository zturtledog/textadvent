package main;

import java.util.HashMap;

public class prettyprint {
    public static String reset="\u001B[0m";

    public static String lnup="\u001B[F";

    public static String bold="\u001B[1m";
    public static String italic="\u001B[3m";
    public static String underline="\u001B[4m";
    public static String invert="\u001B[7m";
    public static String strikethrough="\u001B[9m";
    // export const p = "\u001B[4m"

    public static rinmap<String, String> fg = (new rinmap<String,String>())
        .set("black"  ,"\u001B[30m")
        .set("red"    ,"\u001B[31m")
        .set("green"  ,"\u001B[32m")
        .set("yellow" ,"\u001B[33m")
        .set("orange" ,rgb_fore(251, 191, 119))
        .set("blue"   ,"\u001B[34m")
        .set("magenta","\u001B[35m")
        .set("cyan"   ,"\u001B[36m")
        .set("white"  ,"\u001B[37m")
        .set("grey"   ,rgb_fore(69, 67, 74));

    public static rinmap<String, String> bg = (new rinmap<String,String>())
        .set("black"  ,"\u001B[40m")
        .set("red"    ,"\u001B[41m")
        .set("green"  ,"\u001B[42m")
        .set("yellow" ,"\u001B[43m")
        .set("blue"   ,"\u001B[44m")
        .set("magenta","\u001B[45m")
        .set("cyan"   ,"\u001B[46m")
        .set("white"  ,"\u001B[47m")
        .set("orange" ,rgb_back(251, 191, 119))
        .set("grey"   ,rgb_back(69, 67, 74));

    public static String rgb_back(int r, int g, int b) {return "\u001B[48;2;"+r+";"+g+";"+b+"m";}
    public static String rgb_fore(int r, int g, int b) {return "\u001B[38;2;"+r+";"+g+";"+b+"m";}

    public static itrcla parse_hex(String str) {
        String clean = str.replaceAll("#", "");
        if (clean.length() > 5) {
            return (new itrcla(
                Integer.parseInt(clean.substring(0, 2),16), 
                Integer.parseInt(clean.substring(2, 4),16), 
                Integer.parseInt(clean.substring(4, 6),16)));
        }
        return new itrcla(0, 0, 0);
    }

    public static String assemble(String src) {
        String[] data = src.split("/");
        String text = "";

        for (int i = 0; i < data.length; i++) {
            if (data[i].equals("*n")) {
                text+="\n";
            }
            else if (data[i].equals("*r")) {
                text+=reset;
            }
            else if (data[i].equals("*slash")) {
                text+="/";
            }
            else if (data[i].equals("*star")) {
                text+="*";
            }
            else if (data[i].equals("*at")) {
                text+="@";
            }
            else if (data[i].equals("*arrow")) {
                text+=">";
            }
            else if (data[i].equals(">b")) {text += bold;}
            else if (data[i].equals(">i")) {text += italic;}
            else if (data[i].equals(">u")) {text += underline;}
            else if (data[i].equals(">x")) {text += invert;}
            else if (data[i].equals(">s")) {text += strikethrough;}
            else if (data[i].equals("@cl")) {//change text color
                itrcla ght = parse_hex(data[i+1]);

                text+=rgb_fore(ght.r,ght.g,ght.b)+data[i+2];

                i+=2;
            }
            else if (data[i].equals("@c;")) {//change text color
                text+=fg.get(data[i+1])+data[i+2];

                i+=2;
            }
            else if (data[i].equals("@b;")) {//change text color
                text+=bg.get(data[i+1])+data[i+2];

                i+=2;
            }
            else if (data[i].equals("@bl")) {//change background color
                itrcla ght = parse_hex(data[i+1]);

                text+=rgb_back(ght.r,ght.g,ght.b)+data[i+2];

                i+=2;
            }
            else {
                text+=data[i];
            }
        }

        return text;
    }

    public static String clip(String src) {
        String[] data = src.split("/");
        String text = "";

        for (int i = 0; i < data.length; i++) {
            if (data[i].equals("*n")) {
                text+="\n";
            }
            else if (data[i].equals("*slash")) {
                text+="/";
            }
            else if (data[i].equals("*star")) {
                text+="*";
            }
            else if (data[i].equals("*at")) {
                text+="@";
            }
            else if (data[i].equals("*arrow")) {
                text+=">";
            }
            else if (
                data[i].equals("*r")||
                data[i].equals(">b")||
                data[i].equals(">i")||
                data[i].equals(">u")||
                data[i].equals(">x")||
                data[i].equals(">s")) {}
            else if (data[i].equals("@cl")) {//change text color
                i+=2;
            }
            else if (data[i].equals("@c;")) {//change text color
                i+=2;
            }
            else if (data[i].equals("@b;")) {//change text color
                i+=2;
            }
            else if (data[i].equals("@bl")) {//change background color
                i+=2;
            }
            else {
                text+=data[i];
            }
        }

        return text;
    }

    static class itrcla {
        int r=0,g=0,b=0;
        public itrcla(int r,int g,int b) {
            this.r=r;this.g=g;this.b=b;
        }
    }

    public static class rinmap<T,R> {
        HashMap<T, R> map = new HashMap<T, R>();
        public rinmap() {map = new HashMap<T, R>();}
        public rinmap<T,R> set(T key, R val) {
            map.put(key, val);
            return this;
        }
        public R get(T key) {return map.get(key);}
    }
}