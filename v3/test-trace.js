var trace = {"code": "x = 6\ny = 10\nz = x + y\n", "trace": [{"ordered_globals": [], "stdout": "", "func_name": "<module>", "stack_to_render": [], "globals": {}, "heap": {}, "line": 1, "event": "step_line"}, {"ordered_globals": ["x"], "stdout": "", "func_name": "<module>", "stack_to_render": [], "globals": {"x": 5}, "heap": {}, "line": 2, "event": "step_line"}, {"ordered_globals": ["x", "y"], "stdout": "", "func_name": "<module>", "stack_to_render": [], "globals": {"y": 10, "x": 5}, "heap": {}, "line": 3, "event": "step_line"}, {"ordered_globals": ["x", "y", "z"], "stdout": "", "func_name": "<module>", "stack_to_render": [], "globals": {"y": 10, "x": 5, "z": 15}, "heap": {}, "line": 3, "event": "return"}]};
