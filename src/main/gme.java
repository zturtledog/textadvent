package main;//confussedParr0tFish

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import main.datalng.nxt;

public class gme {
    Scanner inp = new Scanner(System.in);
    datalng datastate;
    Map<String, String> localvars = new HashMap<String, String>();
    String cpath = "advent:";
    ArrayList<String> endings = new ArrayList<String>();
    
    //prgvar
    private String savename;

    public void load(String path, String savenme) {
        datastate = (new datalng()).file(path);
        
        if (!datastate.exists("settings")) {
            System.out.println("invalid advent file... no settings region");
            System.exit(0);
        }
        if (!
        (datastate.exists("settings:slowtext")&&
        datastate.exists("settings:textspeed"))
        ) {
            System.out.println("invalid advent file... settings region doesn't have some of needed keys  [\slowtext or textspeed]");
            System.exit(0);
        }

        System.out.print("\033[H\033[2J");
        
        if (savenme.equals("*new")) {
            savename = ask("what would you like your save file to be called?")+".dta";
            save();
        }else{
            savename = savenme+".dta";
        }
        
        datalng savedata = (new datalng()).file(savename);//datastate.get("settings:saveloc")+"/"+
        cpath = savedata.get("cpath");
        for (nxt nei : savedata.getpathraw("localreagion").blk) {
            localvars.put(nei.key, nei.val);
        }

        // int endl = Integer.parseInt(datastate.get("endings:length"));
        int endl = datastate.getpathraw("endings").blk.size();
        for (int i = 0; i < endl; i++) {
            if (datastate.exists("endings:"+i))
            endings.add(datastate.get("endings:"+i));
        }

        System.out.println("~save at any time with $save\n~use $exit to exit\n\n");
    }

    private void print(String px) {
        String x = prettyprint.assemble(px);
        if (datastate.get("settings:slowtext").equals("true")) {
            for (int i = 0; i < x.length(); i++) {
                System.out.print(x.charAt(i));
                try {
                    Thread.sleep(Integer.parseInt(datastate.get("settings:textspeed")));
                } catch (InterruptedException e) {
                    System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
                    e.printStackTrace();
                }
            }
            System.out.print("\n");
        }else{
            System.out.println(x);
        }
    }

    private String ask(String x) {
        print(x);
        System.out.print("> ");
        return inp.nextLine();
        // System.out.println("\n");
        // return rtn;
    }

    private String input() {
        System.out.print("> ");
        String ixp = inp.nextLine();
        if (ixp.split(" ")[0].equals("$save")) {
            save();
            print("saved the game!");
            return input();
        }
        if (ixp.split(" ")[0].equals("$exit")) {
            save();
            print("farewell!");
            System.exit(0);
        }
        return ixp;
    }

    //unit actions
    
    private void save() {
        try{
            print(savename);
            File file = new File(savename);//datastate.get("settings:saveloc")+"\\"+
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("cpath:"+cpath+"\n\n");
            bw.write("#localreagion"+"\n");

            for (Map.Entry<String, String> entry : localvars.entrySet()) {
                bw.write("  "+entry.getKey()+":"+entry.getValue()+"\n");
            }

            bw.write("#end");

            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    //parsing facilicite

    private void selectionmode(boolean dpree) {
        /**
         * print lables
         * input
         * 
         * get id and run pr[id] if pr[id]
         * if postrun
         *     run postrun
         * 
         * get id and set path to += id if ./id
         * 
         */
        if (dpree) {
            if (datastate.exists(cpath+"prerun")) {
                runcode(datastate.getpathraw(cpath+"prerun").blk);
            }
        }

        String[] labels = datastate.get(cpath+"labels").split(":");
        for (int i = 0; i < labels.length; i++) {
            print("  ["+i+"]: "+labels[i]);
        }
        int inp;
        try {
            inp = Integer.parseInt("0"+input());//fdgjhfdgjskd
        } catch (Exception e) {
            print("that's not an option, try a number.");
            selectionmode(false);
            return;
        }
        if (!!(Math.abs(inp) > labels.length)) {
            print("the number selected was not an option, try again.");
            selectionmode(false);
            return;
        }

        if (datastate.exists(cpath+"pr"+inp)) {
            runcode(datastate.getpathraw(cpath+"pr"+inp).blk);
        }

        if (datastate.exists(cpath+"postrun")) {
            runcode(datastate.getpathraw(cpath+"postrun").blk);
        }

        cpath += inp+":";
        if (datastate.exists(cpath)) {
            iterate();
        }
    }

    public void iterate() {
        // print(cpath);

        localvars.put("$cpath",cpath);

        if (datastate.exists(cpath+"type")) {
            switch (datastate.get(cpath+"type")) {
                case "open":
                    if (datastate.exists(cpath+"prerun")) {
                        runcode(datastate.getpathraw(cpath+"prerun").blk);
                    }
                    localvars.put("$ans", input());
                    if (datastate.exists(cpath+"postrun")) {
                        runcode(datastate.getpathraw(cpath+"postrun").blk);
                    }
                    if (datastate.exists(cpath+"continue")) {
                        cpath += "continue:";
                        iterate();
                    }
                    break;
    
                case "selection":
                    selectionmode(true);
                    break;
        
                default:
                    selectionmode(true);
                    break;
            } 
        }
    }

    //TODO: outsource / redo
    private void runcode(ArrayList<nxt> cde) {
        for (nxt len : cde) {
            switch (len.key) {
                case "print":
                    print(variate(len.val));
                    break;
                case "end":
                    print("\n"+endings.get(Integer.parseInt(len.val)));
                    System.exit(0);
                    break;
                case "path":
                    cpath = variate(len.val);
                    iterate();
                    return;
                case "var":
                    String[] vardat = len.val.split(",",2);
                    localvars.put(vardat[0], variate(vardat[1]));
                    break;
                case "dec":
                    break;
                case "inc":
                    break;
                case "and":
                    break;
                case "not":
                    break;
                case "concat":
                    break;
                case "if":
                    break;
                case "do-times":
                    break;
            }
        }
    }

    private String variate(String val) {
        String trns = val;
        for (Map.Entry<String, String> entry : localvars.entrySet()) {
            trns = trns.replace(entry.getKey(), entry.getValue());
        }

        return trns.replace("/n", "\n");
    }
}
