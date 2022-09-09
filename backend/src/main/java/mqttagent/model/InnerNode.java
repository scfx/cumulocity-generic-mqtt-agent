package mqttagent.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString()
public class InnerNode extends TreeNode {
    
    @Setter
    @Getter
    private Map <String,TreeNode> childNodes;


    static public InnerNode initTree() {
        InnerNode in = new InnerNode();
        in.setDepthIndex(0);
        in.setLevel("/");
        in.setPreTreeNode(null);
        in.setAbsolutePath("/");
        return in;
    }
    
    public InnerNode() {
        this.childNodes = new HashMap<String,TreeNode>();
    }

    public boolean isMappingNode() {
        return false;
    }

    public TreeNode resolveTopicPath(ArrayList<String> tp) throws ResolveException {
        Set<String> set = childNodes.keySet();
        String joinedSet = String.join(",", set);
        String joinedPath = String.join(",", tp);
        log.info("Trying to resolve : {}, {}", joinedSet, joinedPath);
        if (tp.size() >= 1 ) {
            if ( childNodes.containsKey(tp.get(0))){
                TreeNode tn = childNodes.get(tp.get(0));
                // test if exact match exists for this level
                tp.remove(0);
                return tn.resolveTopicPath(tp);
            } else if (childNodes.containsKey("#")) {
                TreeNode tn = childNodes.get("#");
                // test if wildcard "#" match exists for this level
                tp.remove(0);
                return tn.resolveTopicPath(tp);
            } else {
                throw new ResolveException("Path: " + tp.get(0).toString() + " could not be resolved further!");
            }
        } else {
            throw new ResolveException("Unknown Resolution Error!");
        }
    }

    public void insertMapping( InnerNode currentNode, Mapping mapping, ArrayList<String> levels) throws ResolveException{
        var l = levels.get(0);
        String preToString = ( currentNode == null? "null" : currentNode.toString());
        log.info("Trying to add node: {}, {}, {}, {}", currentNode.getLevel(), l, preToString, levels);
        if (levels.size() == 1){
            MappingNode child = new MappingNode();
            child.setPreTreeNode(currentNode);
            child.setMapping(mapping);
            child.setLevel(l);
            child.setAbsolutePath(currentNode.getAbsolutePath()+l+"/");
            child.setDeviceIdentifierIndex(mapping.indexDeviceIdentifierInTemplateTopic);
            child.setDepthIndex(currentNode.getDepthIndex()+1);
            log.debug("Adding mapNode: {}, {}, {}", currentNode.getLevel(), l, child.toString());
            currentNode.getChildNodes().put(l,child);
        } else if (levels.size() > 1){
            levels.remove(0);
            InnerNode child;
            if (currentNode.getChildNodes().containsKey(l)){
                child = (InnerNode) currentNode.getChildNodes().get(l);
            } else {
                child = new InnerNode();
                child.setPreTreeNode(currentNode);
                child.setLevel(l);
                child.setAbsolutePath(currentNode.getAbsolutePath()+l+"/");
                child.setDepthIndex(currentNode.getDepthIndex()+1);
                log.debug("Adding innerNode: {}, {}, {}", currentNode.getLevel(), l, child.toString());
                currentNode.getChildNodes().put(l,child);
            }
            child.insertMapping(child, mapping, levels);
        } else {
            throw new ResolveException("Could not add mapping to tree: " + mapping.toString());
        }
    }
    public void insertMapping(Mapping mapping) throws ResolveException{
        var path = mapping.templateTopic;
        // if templateTopic is not set use topic instead
        if (path == null || path.equals("")){
            path = mapping.topic;
        }
        ArrayList<String> levels = new ArrayList<String>(Arrays.asList(path.split("/")));
        insertMapping(this, mapping, levels);
    }
}