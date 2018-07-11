# Lightweight OPT server that works on both Python 2 and 3

# to invoke, run 'python bottle_server.py'
# and visit http://localhost:8080/index.html
#
# external dependencies: bottle
#
# easy_install pip
# pip install bottle

# From an OPT user: A couple notes: to get bottle_server.py running,
# I had to replace cStringIO with io and urllib2 with urllib, for
# compatibility from 2.x to 3.x Ii was running from /v3/).

from bottle import route, get, request, run, template, static_file
from subprocess import call, check_call, CalledProcessError
from shutil import copyfile
import StringIO # NB: don't use cStringIO since it doesn't support unicode!!!
import json
import pg_logger
import urllib
import urllib2
import sys
import os
import shutil
from pip._vendor.html5lib.constants import prefixes

wd = 'wd'
blast_opt_frontend = '/Users/reza/PhD/tools/blast-opt-frontend/v3'
blast_classpath = '/Users/reza/tempInfoFlow/informationflowtracer/build/main'
blast_lib = '/Users/reza/tempInfoFlow/informationflowtracer/lib'
junit_class_path = blast_lib + '/junit-4.11.jar:blast-opt-backend/lib/hamcrest-core-1.3.jar'
blast_opt_controller = '/Users/reza/PhD/tools/blast-opt-controller'
blast_opt_controller_classpath = blast_opt_controller + '/bin' 
query_files_path = 'blast-opt-code'
predicate_prototype_file_name = 'PredicatePrototype.java'
query_package_path = 'ch/usi/inf/sape/blastopt/controller/analyzer';
query_class_name = 'Query'
query_file_name = query_class_name+ '.java'
criterion_prototype_file_name = 'CriterionPrototype.java'
slice_pure_predicate_prototype = 'SlicePredicatePrototype'
slice_boolean_predicate_prototype = 'SliceBooleanPredicatePrototype'
query_pure_predicate_prototype = 'QueryPredicatePrototype'
query_boolean_predicate_prototype = 'QueryBooleanPredicatePrototype'
defects4j_projects_home='/Users/reza/temp/wd/projects'

inlined_test_class_name = 'InlinedTest'
inlined_test_method_name = 'test'

#AZM: Launching BLAST for different input cases
def executeInlinedTest():
    print "[execute]:\tInlined Test"
    print request.query.user_script
    
    currentRelativeTargetPath = ''+ wd + '/' + 's' + request.query.session_uuid
      
    if not os.path.exists(currentRelativeTargetPath):
        print >> sys.stderr, 'Making dir'
        os.makedirs(currentRelativeTargetPath)
    
    targetFilePath = currentRelativeTargetPath + '/' + 'InlinedTest.java';
    
    tempFile = open(targetFilePath, 'w')
    tempFile.write(request.query.user_script);
    tempFile.close()
    
    classpath = junit_class_path
    print 'classpath:\t' + classpath
    
    retcode = call(["javac", "-g","-cp", classpath, "-source", "7", "-target", "7", targetFilePath])
    
    if retcode != 0:
        raise Exception("Compile error!")
    
    print 'test.class.name:\t' + inlined_test_class_name
    print 'test.method.name:\t' + inlined_test_method_name
    print 'target.wd:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    
    retcode = call(["ant", "-f", blast_opt_controller, 
                                                    "run", 
                                                    "-Dtest.class="+inlined_test_class_name,
                                                    "-Dtest.method="+inlined_test_method_name,
                                                    "-Dtarget=" + blast_opt_frontend+ '/' + currentRelativeTargetPath])
    print "opt-controller:\t" + str(retcode)
    if retcode != 0:
        raise Exception("Opt-controller failed in running the test!")
    
    print 'Results written into:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    
    return static_file("data.json", root=currentRelativeTargetPath)
    
def executeJarFile():
    print "executing jar file"
    
def executeCustomAddress():
    print "executing custom address"

# dummy routes for testing only
@route('/web_exec_<name:re:.+>.py')
def web_exec(name):
    print >> sys.stderr, '1'
    return 'OK'

@route('/LIVE_exec_<name:re:.+>.py')
def live_exec(name):
    print >> sys.stderr, '2'
    return 'OK'


@route('/<filepath:path>')
def index(filepath):
    print >> sys.stderr, '3'
    # special-case for testing name_lookup.py ...
    if 'name_lookup.py' in filepath:
        return json.dumps(dict(name='TEST NAME', email='TEST EMAIL'))
    return static_file(filepath, root='.')

@get('/defects4j')
def exec_defects4j_test():
    print "Execute defects4j test in server side!!!!"
    selected_defects4j_project = request.query.project_name;
    selected_project_bug_no = request.query.bug_no;
    selected_fvb = request.query.fvb;
    print 'Selected Project\t' + selected_defects4j_project;
    print 'Selected bug no\t' + selected_project_bug_no;
    print 'Selected fvb\t' + selected_fvb;
    
    currentRelativeTargetPath = ''+ wd + '/' + 's' + request.query.session_uuid;
    print 'Relative target path:\t' + currentRelativeTargetPath;
    
    retcode = call(["ant", "-f", blast_opt_controller, 
                                                    "run-defects4j-test", 
                                                    "-Dtest.project="+selected_defects4j_project,
                                                    "-Dbug.no="+selected_project_bug_no,
                                                    "-Dfvb="+selected_fvb,
                                                    "-Dtarget=" + blast_opt_frontend+ '/' + currentRelativeTargetPath])
    
    print "opt-controller:\t" + str(retcode)
    if retcode != 0:
        raise Exception("Opt-controller failed in running the test!")
    
    print 'Results written into:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    print 'working directory:\t' + currentRelativeTargetPath
    
    return static_file("data.json", root=currentRelativeTargetPath)

@get('/query')
def exec_query():
    print "Execute query in server side!!!!"
    currentRelativeTargetPath = ''+ wd + '/' + 's' + request.query.session_uuid;
    print currentRelativeTargetPath;
    
    
    if not os.path.exists(currentRelativeTargetPath):
        raise Exception('Invalid session! Not found any match for the given session!')
    
    query_destination = currentRelativeTargetPath + '/' + query_package_path
    if not os.path.exists(query_destination):
        os.makedirs(query_destination)
        
    #copy query source files after replacing the template with concrete values
    if request.query.slice_predicate_type == 'boolType':
        slice_predicate_prototype = slice_boolean_predicate_prototype;
    else:
        slice_predicate_prototype = slice_pure_predicate_prototype;
    
    slice_predicate_prototype_file = open(query_files_path + '/' + slice_predicate_prototype + '.java', 'r')
    slice_predicate_param_value = request.query.slice_predicate
    slice_predicate_concrete_text = slice_predicate_prototype_file.read().replace('/*...*/', slice_predicate_param_value, 1)
    slice_predicate_concrete_file = open(query_destination + '/' + slice_predicate_prototype + '.java', 'w')
    slice_predicate_concrete_file.write(slice_predicate_concrete_text);
    slice_predicate_concrete_file.close()
    slice_predicate_prototype_file.close()
    
    if request.query.query_predicate_type == 'boolType':
        query_predicate_prototype = query_boolean_predicate_prototype;
    else:
        query_predicate_prototype = query_pure_predicate_prototype;
        
    query_predicate_prototype_file = open(query_files_path + '/' + query_predicate_prototype + '.java', 'r')
    query_predicate_param_value = request.query.query_predicate
    query_predicate_concrete_text = query_predicate_prototype_file.read().replace('/*...*/', query_predicate_param_value, 1)
    query_predicate_concrete_file = open(query_destination + '/' + query_predicate_prototype + '.java', 'w')
    query_predicate_concrete_file.write(query_predicate_concrete_text);
    query_predicate_concrete_file.close()
    query_predicate_prototype_file.close()
    
    criterion_prototype_file = open(query_files_path + '/' + criterion_prototype_file_name, 'r')
    criterion_absIdx_value = request.query.criterion_ae_idx
    criterion_bcIdx_value = request.query.criterion_bc_idx
    criterion_concrete_text = criterion_prototype_file.read();
    print "criterion_absIdx_value" + criterion_absIdx_value
    print "criterion_bcIdx_value" + criterion_bcIdx_value
    
    criterion_concrete_text = criterion_concrete_text.replace('/*aeidx...*/', criterion_absIdx_value, 1)
    criterion_concrete_text = criterion_concrete_text.replace('/*bcidx...*/', criterion_bcIdx_value, 1)
    
    criterion_concrete_file = open(query_destination + '/' + criterion_prototype_file_name, 'w')
    criterion_concrete_file.write(criterion_concrete_text);
    criterion_concrete_file.close()
    criterion_prototype_file.close()
    
    
    target_query_prototype_file = open(query_files_path + '/' + query_file_name, 'r')
    target_query_file_text = target_query_prototype_file.read() 
    target_query_concrete_text = target_query_file_text.replace('/*s...*/', slice_predicate_prototype, 1)
    target_query_concrete_text = target_query_concrete_text.replace('/*q...*/', query_predicate_prototype, 1)
    target_query_file_address = query_destination + '/' + query_file_name
    target_query_file = open(target_query_file_address, 'w')
    target_query_file.write(target_query_concrete_text);
    target_query_file.close()
    target_query_prototype_file.close()
    
#     copyfile(query_files_path+'/'+ query_file_name, targetQueryFile)
    classpath = blast_classpath + ':' + blast_opt_controller_classpath + ':' + currentRelativeTargetPath
    
    retcode = call(["javac", "-g","-cp", classpath, "-source", "7", "-target", "7", target_query_file_address])
    
    if retcode != 0:
        raise Exception("Compile error!")
    
    query_fully_qualified_class_name = query_package_path.replace('/', '.')+ '.' + query_class_name
    selected_defects4j_project = request.query.project_name;
    selected_project_bug_no = request.query.bug_no;
    selected_fvb = request.query.fvb;
    input_type = request.query.input_type;
    
    if input_type == 'inlineTestPane':
        retcode = call(["ant", "-f", blast_opt_controller, 
                                                        "run", 
                                                        "-Dtest.class="+inlined_test_class_name,
                                                        "-Dtest.method="+inlined_test_method_name,
                                                        "-Dtest.analyzer="+query_fully_qualified_class_name,
                                                        "-Dtarget=" + blast_opt_frontend+ '/' + currentRelativeTargetPath])
    else:
        retcode = call(["ant", "-f", blast_opt_controller, 
                                                        "run-defects4j-test", 
                                                        "-Dtest.project="+selected_defects4j_project,
                                                        "-Dbug.no="+selected_project_bug_no,
                                                        "-Dfvb="+selected_fvb,
                                                        "-Dtest.analyzer="+query_fully_qualified_class_name,
                                                        "-Dtarget=" + blast_opt_frontend+ '/' + currentRelativeTargetPath])
     
    
    print "opt-controller:\t" + str(retcode)
    if retcode != 0:
        raise Exception("Opt-controller failed in running the test!")
    
    print 'Results written into:\t' + blast_opt_frontend+ '/' + currentRelativeTargetPath
    print 'working directory:\t' + currentRelativeTargetPath
    
    return static_file("data.json", root=currentRelativeTargetPath)
    
@get('/src')
def get_src():
    
    prefixes = [
                'src/test/java',
                'src/main/java', 
                'source',
                'tests',
                ];
    
    src_name = request.query.source_name;
    selected_defects4j_project = request.query.project_name;
    selected_project_bug_no = request.query.bug_no;
    selected_fvb = request.query.fvb;
    found = False;
    
    print 'get source for:\t'+ request.query.project_name + ':' + selected_project_bug_no+':' + selected_fvb
    
    
    for prefix in prefixes:
        addrs = defects4j_projects_home+ '/' + selected_defects4j_project + '/' + selected_project_bug_no + '/' + selected_fvb + '/' + prefix + '/' + src_name;
        print 'Testing address:\t' + addrs
         
        if os.path.exists(addrs):
            found = True;
            print 'address matched' 
            break; 
        
    
    if found:
        root = defects4j_projects_home+ '/' + selected_defects4j_project + '/' + selected_project_bug_no + '/' + selected_fvb + '/' + prefix;
        return static_file(src_name, root)
    else:
        return 'not found'
    
#         
#         var url = prefix + '/' + source;
#             $.get(url, function(data){
#                 sourceEditorSetValue(data);
#                 myVisualizer.sourceCache[url] = data;
#             })
#         } else {
#             sourceEditorSetValue(sourceContent);
#         }

    

    

@get('/exec') 
def get_exec():
    print "Hi from Python"
    print >> sys.stderr, 'ID:' 
    print "classpath:\t" + request.query.class_path
    print >> sys.stderr, get_exec.id
    print "sessionId:\t" + request.query.session_uuid
    
    input_type = request.query.input_type
    if input_type == 'inlineTestPane':
        return executeInlinedTest()
    elif input_type == 'jarFilePane':
        return executeJarFile()
    elif input_type == 'customAddressPane':
        return executeCustomAddress()
    else:
        raise Exception('Invalid input type specified!')   
       
    
    

@get('/load_matrix_problem.py')
def load_matrix_problem():
  print >> sys.stderr, '5'
  prob_name = request.query.problem_name
  assert type(prob_name) in (str, unicode)

  # whitelist
  assert prob_name in ('python_comprehension-1',)

  fn = 'matrix-demo/' + prob_name + '.py'
  f = open(fn)
  cod = f.read()
  f.close()

  import doctest
  import sys
  p = doctest.DocTestParser()
  examples = p.get_examples(cod)
  if len(examples):
    first_ex = examples[0]
    #print >> sys.stderr, 'Source:', `first_ex.source`
    testCod = 'result = ' + first_ex.source

  return json.dumps(dict(code=cod, test=testCod))


get_exec.id = 1

@get('/submit_matrix_problem.py')
def submit_matrix_problem():
  print >> sys.stderr, '6'
  user_code = request.query.submitted_code
  prob_name = request.query.problem_name
  assert type(prob_name) in (str, unicode)

  # whitelist
  assert prob_name in ('python_comprehension-1',)

  test_fn = 'matrix-demo/' + prob_name + '.test.py'
  test_cod = open(test_fn).read()

  # concatenate!
  script = test_cod + '\n' + user_code + \
'''
import doctest
(n_fail, n_tests) = doctest.testmod(verbose=False)
if n_fail == 0:
    print("All %d tests passed!" % n_tests)
'''

  url = 'http://ec2-107-20-94-197.compute-1.amazonaws.com/cgi-bin/run_code.py'
  values = {'user_script' : script}

  data = urllib.urlencode(values)
  req = urllib2.Request(url, data)
  response = urllib2.urlopen(req)
  the_page = response.read()
  return the_page


if __name__ == "__main__":
    #run(host='localhost', port=8080, reloader=True)
    run(host='0.0.0.0', port=8003, reloader=True) # make it externally visible - DANGER this is very insecure since there's no sandboxing!
    
