# ytools
theFAQApp is meant to a be a fairly simple FAQ tool, which allows admins to create modules, then submodules, and information associated.
Everything is stored in a JSON file. The mapping has been done using BOON project.
The information uses Alex Gorbatchev's syntax highligter to highlight code snippets.

Here is the sample Json file containing data:


    {
      "modules": {
        "module1": {
          "sub-module1": {
            "info": "some faq statememt"
          },
          "sub-module2": {
            "info": "some faq statememt"
          }
        },
        "module2": {
          "sub-module1": {
            "info": "info on module2: submodule1"
          }
        }
      }
    }


You can see actual fields in model.ModulesData, model.Module and model.SubModule.
