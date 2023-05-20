package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class datalng {
    private ArrayList<nxt> internal = new ArrayList<nxt>();
    private ArrayList<cllsific> varx = new ArrayList<cllsific>();

    // initilizer

    public datalng file(String path) {
        ArrayList<String> strings = new ArrayList<String>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                strings.add(myReader.nextLine().trim());
            }
            myReader.close();

            parse(strings);
        } catch (FileNotFoundException e) {
            System.out.println(prettyprint.assemble("/@c;/red/An error occurred. (file not found, this could be because of a typo or nonexistent path)/*r/"));
            e.printStackTrace();
            System.exit(0);
        }

        return this;
    }

    // internal functions

    private void parse(ArrayList<String> lines) {//-undone
        ArrayList<cllsific> clli = new ArrayList<cllsific>();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).length() > 1) {
                // System.out.println(lines.get(i).charAt(0));    

                if (lines.get(i).charAt(0) == '#') {
                    if (lines.get(i).compareTo("#end")==0) {//a tail
                        // System.out.println("tail");
                        clli.add(new cllsific(lines.get(i), "tail"));
                    }else{//a normal head
                        // System.out.println("head: '"+lines.get(i)+"'");
                        clli.add(new cllsific(lines.get(i), "head"));
                    }
                }else if (lines.get(i).charAt(0) == '~') {
                    //this is a comment
                } else if (lines.get(i).charAt(0) == '.') {//a varible
                    // System.out.println("varible");
                    String[] vardata = lines.get(i).split(" ", 3);
                    varx.add(new cllsific(vardata[0].split(".",2)[1], vardata[2]));
                } else{//a key
                    // System.out.println("key");
                    clli.add(new cllsific(lines.get(i), "key"));
                }
            }    
        }
        internal = block(clli);
    }

    private ArrayList<nxt> block(ArrayList<cllsific> clis) {
        ArrayList<cllsific> nextac = new ArrayList<cllsific>();
        ArrayList<nxt> end = new ArrayList<nxt>();
        int depth = 0;
        String head = "";
        boolean parsing = false;
        for (int i = 0; i < clis.size(); i++) {
            if (!clis.get(i).type.equals("key")) {
                // System.out.println(clis.get(i).value);
                if (clis.get(i).type.equals("tail")) {// tails 
                    depth--;
                    if (parsing) {
                        if (depth == 0) {
                            parsing = false;
                            end.add(new nxt(head,block(nextac)));
                            nextac.clear();
                        }else{
                            nextac.add(clis.get(i));
                        }
                    }
                }else{// heads
                    depth++;
                    if (parsing) {
                        nextac.add(clis.get(i));
                    }else{
                        head = clis.get(i).value.replaceFirst("#", "");
                    }
                    parsing = true;
                }
            }else{//keys and locks
                if (parsing) {
                    nextac.add(clis.get(i));
                }else{
                    String[] srdata = clis.get(i).value.split(":",2);
                    end.add(new nxt(srdata[0], variate(srdata[1])));
                }
            }
        }
        return(end);
    }

    private nxt findinarraylist(String key, ArrayList<nxt> list) {
        for (nxt nei : list) {
            if (nei.key.equals(key)) {
                return nei;
            }
        }
        throw new Error("key("+key+") not found in list");
    }

    private String variate(String inx) {
        String trns = inx;
        for (cllsific mal : varx) {
            trns = trns.replace(mal.value, mal.type);
        }

        return trns.replace("/n", "\n");
    }

    // external functions

    public String get(String path) {//head:head1:key
        // System.out.println(getpathraw(path).val);

        return(getpathraw(path).val);
    }

    public nxt getpathraw(String path) {
        String[] pathdat = path.split(":");
        nxt temp = new nxt("hi", internal);

        for (int i = 0; i < pathdat.length; i++) {
            temp = findinarraylist(pathdat[i],temp.blk);
        }

        return temp;
    }

    public ArrayList<nxt> RAW() {
        return internal;
    }

    public boolean exists(String path) {
        String[] pathdat = path.split(":");
        nxt temp = new nxt("hi", internal);

        for (int i = 0; i < pathdat.length; i++) {
            for (nxt nei : temp.blk) {
                if (nei.key.equals(pathdat[i])) {
                    temp = nei;

                    // System.out.println(temp.toString());
                    // System.out.println(nei.key);
                    
                    if (i == pathdat.length-1) {
                        return true;
                    }

                    break;
                }
            }
        }

        return !true;
    }

    // internal classes

    /**
     * nxt
     * 
     * -undone
     */
    public class nxt {
        boolean isblock = false;
        public String key = "";
        public String val = "";
        public ArrayList<nxt> blk = new ArrayList<nxt>();

        public nxt(String key, String val) {
            this.key = key;
            this.val = val;
        }

        public nxt(String key, ArrayList<nxt> block) {
            isblock = true;
            this.key = key;
            blk = block;
        }

        public String toString() {
            return("block?: "+isblock+", key: "+key+(isblock?", block: "+blk.toString():", val: "+val));
        }
    }

    /**
     * cllsisic
     */
    public class cllsific {
        public String value;
        public String type;

        public cllsific(String val, String typ) {
            value = val;
            type = typ;
        }

        public String tostring() {
            return ("value: "+value+", type: "+type);
        }
    }
}
